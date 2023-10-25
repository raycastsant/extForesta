package org.geocuba.foresta.analisis_terreno.interseccion.recortes.raster;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import org.cresques.cts.IProjection;
import org.gvsig.fmap.raster.layers.DefaultLayerConfiguration;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.fmap.raster.layers.IConfiguration;
import org.gvsig.fmap.raster.layers.ISolveErrorListener;
import org.gvsig.fmap.raster.layers.NotAvailableStateException;
import org.gvsig.raster.buffer.BufferFactory;
import org.gvsig.raster.dataset.CompositeDataset;
import org.gvsig.raster.dataset.FileNotOpenException;
import org.gvsig.raster.dataset.IBuffer;
import org.gvsig.raster.dataset.MosaicNotValidException;
import org.gvsig.raster.dataset.MultiRasterDataset;
import org.gvsig.raster.dataset.NotSupportedExtensionException;
import org.gvsig.raster.dataset.io.RasterDriverException;
import org.gvsig.raster.dataset.properties.DatasetColorInterpretation;
import org.gvsig.raster.datastruct.ColorTable;
import org.gvsig.raster.datastruct.persistence.ColorTableLibraryPersistence;
import org.gvsig.raster.grid.Grid;
import org.gvsig.raster.grid.GridException;
import org.gvsig.raster.grid.GridPalette;
import org.gvsig.raster.grid.GridTransparency;
import org.gvsig.raster.grid.filter.FilterTypeException;
import org.gvsig.raster.grid.filter.RasterFilterList;
import org.gvsig.raster.grid.filter.RasterFilterListManager;
import org.gvsig.raster.grid.filter.bands.ColorTableListManager;
import org.gvsig.raster.grid.filter.enhancement.EnhancementStretchListManager;
import org.gvsig.raster.grid.filter.enhancement.LinearStretchParams;
import org.gvsig.raster.grid.render.Rendering;
import org.gvsig.raster.hierarchy.IStatistics;
import org.gvsig.raster.util.RasterToolsUtil;
import com.iver.cit.gvsig.exceptions.layers.LoadLayerException;
import com.iver.cit.gvsig.fmap.layers.FLayer;

@SuppressWarnings("unchecked")
public class MapRasterLayer extends FLyrRasterSE{
	
	private Object params = null;	
	private static Hashtable<Class, ISolveErrorListener> 
    solveListeners = new Hashtable<Class, ISolveErrorListener>();
	static private IConfiguration configuration = new DefaultLayerConfiguration();
	
	public static MapRasterLayer createLayer(String layerName, Object params,
			IProjection proj) throws LoadLayerException {
		MapRasterLayer capa = new MapRasterLayer();
		capa.setLoadParams(params);
		capa.setName(layerName);
		capa.setProjection(proj);
		capa.load();
		return capa;
	}
	
	public void load() throws LoadLayerException {
		if (isStopped())
			return;

		enableStopped(); // Paramos la capa mientras se hace un load

		String fName = null;
		int test = -1;
		if (params != null && params instanceof File) {
			fName = ((File) params).getAbsolutePath();
			test = fName.indexOf("ecwp:");
		}

		if (test != -1) {
			String urlECW = fName.substring(test + 6);
			fName = "ecwp://" + urlECW;
			System.err.println(test + " " + fName);
		}

		try {
			if (params instanceof String[][]) {
				String[][] files = (String[][]) params;
				MultiRasterDataset[][] dt = new MultiRasterDataset[files.length][files[0].length];
				for (int i = 0; i < files.length; i++)
					for (int j = 0; j < files[i].length; j++)
						dt[i][j] = MultiRasterDataset.open(getProjection(), files[i][j]);
				dataset = new CompositeDataset(dt);
			} else
				if (params == null || params instanceof File) {
					if (fName != null)
						dataset = MultiRasterDataset.open(getProjection(), fName);
				} else
					dataset = MultiRasterDataset.open(getProjection(), params);
		} catch (NotSupportedExtensionException e) {
			if(test == -1 ) { 
				FLyrRasterSE lyr = tryToSolveError(e, this);
				if(lyr != null)
					dataset = lyr.getDataSource();
				else
					throw new LoadLayerException("Formato no valido", e);
			} else
				throw new LoadLayerException("Formato no valido", e);
		} catch (MosaicNotValidException e) {
			throw new LoadLayerException("Error en el mosaico", e);
		} catch (Exception e) {
			throw new LoadLayerException("No existe la capa.", e);
		}
	}
	
	private static FLyrRasterSE tryToSolveError(Exception e, FLayer layer) {
		ISolveErrorListener sel = solveListeners.get(e.getClass());
		if (sel != null) {
			FLyrRasterSE solvedLayer = null;
			solvedLayer = (FLyrRasterSE)sel.solve(layer, null);
			if (solvedLayer != null && sel != null){
				return solvedLayer;
			}
		}
		layer.setAvailable(false);
		return (FLyrRasterSE)layer;
	}
	
	public void init(DatasetColorInterpretation ci) throws LoadLayerException {
		if (dataset == null)
			throw new LoadLayerException("Formato no valido", new IOException());
		
		dataset.getDataset(0)[0].setColorInterpretation(ci);
		bufferFactory = new BufferFactory(dataset);
		render = new Rendering(bufferFactory);
		render.addVisualPropertyListener(this);
		initFilters();

		//Inicialización del historico de transformaciones
		getAffineTransformHistorical().clear();
		getAffineTransformHistorical().add(this.getAffineTransform());

		try {
			enableOpen();
		} catch (NotAvailableStateException e) {
			RasterToolsUtil.messageBoxError("Fallo el estado de open. Closed=" + isClosed() + " Awake=" + isAwake(), this, e);
		}
	}
	
	private void initFilters() {
		RasterFilterList filterList = new RasterFilterList();
		filterList.addEnvParam("IStatistics", getDataSource().getStatistics());
		filterList.addEnvParam("MultiRasterDataset", getDataSource());
		
		if(getDataSource() == null)
			return;
		
		getDataSource().resetNoDataValue();
		applyNoData();
		GridTransparency gridTransparency = new GridTransparency(getDataSource().getTransparencyFilesStatus());

		filterList.setInitDataType(getDataType()[0]);
		RasterFilterListManager filterManager = new RasterFilterListManager(filterList);

		// Quitamos la leyenda
		lastLegend = null;

		try {
			//Si en la carga del proyecto se cargó una tabla de color asignamos esta
			if(loadedFromProject != null) {
				GridPalette p = new GridPalette(loadedFromProject);
				setLastLegend(p);
				ColorTableListManager ctm = new ColorTableListManager(filterManager);
				ctm.addColorTableFilter(p);
			} else {
				//sino ponemos la tabla asociada al raster
				if (this.getDataSource().getColorTables()[0] != null) {
					GridPalette p = new GridPalette(getDataSource().getColorTables()[0]);
					setLastLegend(p);
					ColorTableListManager ctm = new ColorTableListManager(filterManager);
					ctm.addColorTableFilter(p);
				} else {
					//sino hace lo que dice en las preferencias
					if (getDataType()[0] != IBuffer.TYPE_BYTE) 
						loadEnhancedOrColorTable(filterManager);

				}
			}
			loadedFromProject = null;

			getRender().setFilterList(filterList);
			// Inicializo la transparencia para el render
			getRender().setLastTransparency(gridTransparency);
		} catch (FilterTypeException e) {
			//Ha habido un error en la asignación de filtros por los que no se añade ninguno.
			RasterToolsUtil.debug("Error añadiendo filtros en la inicialización de capa " + this.getName() + " Datatype=" + this.getDataType(), null, e);
		}
	}
	
	private void loadEnhancedOrColorTable(RasterFilterListManager filterManager) throws FilterTypeException {
		String colorTableName = configuration.getValueString("loadlayer_usecolortable", (String) null);

		String palettesPath = System.getProperty("user.home") +
			File.separator +
			"gvSIG" + // PluginServices.getArguments()[0] +
			File.separator + "colortable";

		IStatistics stats = getDataSource().getStatistics();

		if (colorTableName != null) {
			try {
				stats.calcFullStatistics();
				if (getBandCount() == 1) {
					ArrayList fileList = ColorTableLibraryPersistence.getPaletteFileList(palettesPath);
					for (int i = 0; i < fileList.size(); i++) {
						ArrayList paletteItems = new ArrayList();
						String paletteName = ColorTableLibraryPersistence.loadPalette(palettesPath, (String) fileList.get(i), paletteItems);
						if (paletteName.equals(colorTableName)) {
							if (paletteItems.size() <= 0)
								continue;

							ColorTable colorTable = new ColorTable();
							colorTable.setName(paletteName);
							colorTable.createPaletteFromColorItems(paletteItems, true);
							colorTable.setInterpolated(true);

							colorTable.createColorTableInRange(stats.getMinimun(), stats.getMaximun(), true);

							GridPalette p = new GridPalette(colorTable);
							setLastLegend(p);

							ColorTableListManager ctm = new ColorTableListManager(filterManager);
							ctm.addColorTableFilter(p);
							return;
						}
					}
				}
			} catch (FileNotOpenException e) {
				// No podemos aplicar el filtro
			} catch (RasterDriverException e) {
				// No podemos aplicar el filtro
			} catch (InterruptedException e) {
				// El usuario ha cancelado el proceso
			}
		}

		/*EnhancementListManager elm = new EnhancementListManager(filterManager);
		elm.addEnhancedFilter(false, stats, 0.0, getRender().getRenderBands());*/
		
		EnhancementStretchListManager elm = new EnhancementStretchListManager(filterManager);
		try {
			elm.addEnhancedStretchFilter(LinearStretchParams.createStandardParam(getRenderBands(), 0.0, stats, false), 
										stats, 
										getRender().getRenderBands(), 
										false);
		} catch (FileNotOpenException e) {
			//No podemos aplicar el filtro
		} catch (RasterDriverException e) {
			//No podemos aplicar el filtro
		}
	}

	public void setLoadParams(Object param){
		this.params = param;

		//Si la capa tiene nombre acivamos el estado awake
		if(params != null && getName() != null) {
			try {
				enableAwake();
			} catch (NotAvailableStateException e) {
				RasterToolsUtil.messageBoxError("Fallo el estado de open. Closed=" + isClosed() + " Active=" + isOpen(), this, e);
			}
		}
	}
	
	public void setBufferFactory(BufferFactory bf)
	{
		this.bufferFactory = bf;
		render.setBufferFactory(bufferFactory);
	}
	
	public Grid getGrid(double x, double y, double width, double height, boolean interpolated) throws GridException, InterruptedException {
		BufferFactory bf = getBufferFactory();
		bf.clearDrawableBand();
		bf.setAllDrawableBands();
		try {
			bf.setAreaOfInterest(x,y,width,height);
		} catch (Exception e) {
			throw new GridException("Error reading buffer");
		}
		return new Grid(bf, interpolated);
	}
}
