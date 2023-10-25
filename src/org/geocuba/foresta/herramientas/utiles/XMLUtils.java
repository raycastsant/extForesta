package org.geocuba.foresta.herramientas.utiles;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.channels.FileChannel;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

import com.iver.andami.ConfigurationException;
import com.iver.andami.messages.Messages;
import com.iver.andami.messages.NotificationManager;
import com.iver.utiles.DateTime;
import com.iver.utiles.XMLEntity;
import com.iver.utiles.xml.XMLEncodingUtils;
import com.iver.utiles.xmlEntity.generate.XmlTag;

public class XMLUtils 
{
	public static XMLEntity persistenceFromXML(String file_path) throws ConfigurationException, FileNotFoundException {
		File xml = new File(file_path);

		if (xml.exists()) {
			InputStreamReader reader = null;

			try {
				reader = XMLEncodingUtils.getReader(xml);
				XmlTag tag = (XmlTag) XmlTag.unmarshal(reader);
				return new XMLEntity(tag);
			} catch (FileNotFoundException e) {
				throw new ConfigurationException(e);
			} catch (MarshalException e) {

				// try to reopen with default encoding (for backward compatibility)
				try {
					reader = new FileReader(xml);
					XmlTag tag = (XmlTag) XmlTag.unmarshal(reader);
					return new XMLEntity(tag);

				} catch (MarshalException ex) {
					// try to close the stream, maybe it remains open
					if (reader!=null) {
						try { reader.close(); } catch (IOException e1) {}
					}
					// backup the old file
					String backupFile = file_path+"-"+DateTime.getCurrentDate().getTime();
					NotificationManager.addError(Messages.getString("Error_reading_plugin_persinstence_New_file_created_A_backup_was_made_on_")+backupFile, new ConfigurationException(e));
					xml.renameTo(new File(backupFile));
					// create a new, empty configuration
					return new XMLEntity();
				}
				catch (FileNotFoundException ex) {
					return new XMLEntity();
				} catch (ValidationException ex) {
					throw new ConfigurationException(e);
				}
			} catch (ValidationException e) {
				throw new ConfigurationException(e);
			}
		} else {
			throw new FileNotFoundException("No se encontró el fichero de configuración");
			//return new XMLEntity();
		}
	}

	public  static void persistenceToXML(XMLEntity entity,String file_path)
	throws ConfigurationException {

		String CASTORENCODING="UTF8";
		// write on a temporary file in order to not destroy current file if there is some problem while marshaling
		File tmpFile = new File(file_path + DateTime.getCurrentDate().getTime());

		File xml = new File(file_path);
		OutputStreamWriter writer = null;

		try {
			writer = new OutputStreamWriter(new FileOutputStream(tmpFile), CASTORENCODING);
			entity.getXmlTag().marshal(writer);
			writer.close();

			// if marshaling process finished correctly, move the file to the correct one
			xml.delete();
			if (!tmpFile.renameTo(xml)) {
				// if rename was not succesful, try copying it
				FileChannel sourceChannel = new  FileInputStream(tmpFile).getChannel();
				FileChannel destinationChannel = new FileOutputStream(xml).getChannel();
				sourceChannel.transferTo(0, sourceChannel.size(), destinationChannel);
				sourceChannel.close();
				destinationChannel.close();

			}
		} catch (FileNotFoundException e) {
			throw new ConfigurationException(e);
		} catch (MarshalException e) {
			// try to close the stream, maybe it remains open
			if (writer!=null) {
				try { writer.close(); } catch (IOException e1) {}
			}
		} catch (ValidationException e) {
			throw new ConfigurationException(e);
		} catch (IOException e) {
			throw new ConfigurationException(e);
		}
	}
}
