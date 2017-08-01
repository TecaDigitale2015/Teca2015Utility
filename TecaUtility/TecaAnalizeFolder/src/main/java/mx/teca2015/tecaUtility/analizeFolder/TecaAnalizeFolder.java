/**
 * 
 */
package mx.teca2015.tecaUtility.analizeFolder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.UUID;

import javax.naming.NamingException;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.exception.ConstraintViolationException;

import it.sbn.iccu.metaag1.Metadigit;
import mx.randalf.configuration.exception.ConfigurationException;
import mx.randalf.mag.MagXsd;
import mx.randalf.xsd.exception.XsdException;
import mx.teca2015.database.tecaStorage.dao.FilesStorageDAO;
import mx.teca2015.database.tecaStorage.entity.FilesStorage;

/**
 * @author massi
 *
 */
public class TecaAnalizeFolder {

	private Logger log = Logger.getLogger(TecaAnalizeFolder.class);

	/**
	 * 
	 */
	public TecaAnalizeFolder() {
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TecaAnalizeFolder taf = null;

		if (args.length==1){
			taf = new TecaAnalizeFolder();
			taf.esegui(args[0]);
		} else {
			System.out.println("Indicare il file da analizzare");
		}
	}

	public void esegui(String fileName) {
		BufferedReader br = null;
		FileReader fr = null;
		File f= null;
		File fLine = null;
		String line = null;
		int conta =0;

		try {
			f = new File(fileName);
			if (f.exists()){
				fr = new FileReader(f);
				br = new BufferedReader(fr);
				while((line = br.readLine()) !=null){
					conta++;
					if ((conta%100)==0){
						System.out.println("Conta: "+conta);
					}
					fLine = new File(line);
					addFilesStorage(fLine.getName(), fLine);
				}
			} else {
				System.err.println("Il file ["+f.getAbsolutePath()+"] non esiste");
			}
		} catch (FileNotFoundException e) {
			log.error(e.getMessage(), e);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		} catch (ConstraintViolationException e) {
			log.error(e.getMessage(), e);
		} catch (HibernateException e) {
			log.error(e.getMessage(), e);
		} catch (NamingException e) {
			log.error(e.getMessage(), e);
		} catch (ConfigurationException e) {
			log.error(e.getMessage(), e);
		} catch (XsdException e) {
			log.error(e.getMessage(), e);
		} finally {
			try {
				if (br != null){
					br.close();
				}
				if (fr != null){
					fr.close();
				}
			} catch (IOException e) {
				log.error(e.getMessage(), e);
			}
		}
	}

	private void addFilesStorage(String fileXml, File path) throws ConstraintViolationException, HibernateException, NamingException, ConfigurationException, XsdException{
		FilesStorageDAO filesStorageDAO = null;
		FilesStorage filesStorage = null;
		MagXsd magXsd = null;
		Metadigit mag = null;
		
		try {
			filesStorageDAO = new FilesStorageDAO(null);
			filesStorage = filesStorageDAO.find(fileXml, path.getAbsolutePath());
			if (filesStorage == null || filesStorage.getBid()==null){
				if (path.exists()){
					magXsd = new MagXsd();
					mag = magXsd.read(path);
					
					if (mag != null){
						if (filesStorage == null){
			//				filesStorageDAO.beginTransaction();
							filesStorage = new FilesStorage();
							filesStorage.setId(UUID.randomUUID().toString());
							filesStorage.setFileXml(fileXml);
							filesStorage.setPath(path.getAbsolutePath());
			//				filesStorageDAO.commitTransaction();
						}
						if (mag.getBib() != null){
							if (mag.getBib().getIdentifier() != null &&
									mag.getBib().getIdentifier().size()>0 && 
									mag.getBib().getIdentifier().get(0).getContent() != null &&
									mag.getBib().getIdentifier().get(0).getContent().size()>0){
								filesStorage.setBid(mag.getBib().getIdentifier().get(0).getContent().get(0));
								if (mag.getBib().getPiece() != null){
									if (mag.getBib().getPiece().getIssue() != null &&
											mag.getBib().getPiece().getYear() != null){
										filesStorage.setPiece(mag.getBib().getPiece().getYear());
										filesStorage.setPiece2(mag.getBib().getPiece().getIssue());
										if (mag.getBib().getPiece().getStpiecePer() != null){
											filesStorage.setPieceIn(mag.getBib().getPiece().getStpiecePer());
										}
									} else if (mag.getBib().getPiece().getPartName() != null &&
											mag.getBib().getPiece().getPartNumber() != null){
										filesStorage.setPiece(mag.getBib().getPiece().getPartNumber().toString());
										filesStorage.setPiece2(mag.getBib().getPiece().getPartName());
										if (mag.getBib().getPiece().getStpieceVol() != null){
											filesStorage.setPieceIn(mag.getBib().getPiece().getStpieceVol());
										}
									}
								}
								if (mag.getBib().getHoldings() != null &&
										mag.getBib().getHoldings().size()>0){
									if (mag.getBib().getHoldings().get(0).getInventoryNumber() != null){
										filesStorage.setInventario(mag.getBib().getHoldings().get(0).getInventoryNumber());
									}
									if (mag.getBib().getHoldings().get(0).getShelfmark() != null && 
											mag.getBib().getHoldings().get(0).getShelfmark().size()>0){
										filesStorage.setCollocazione(mag.getBib().getHoldings().get(0).getShelfmark().get(0).getContent());
									}
								}
								filesStorageDAO.update(filesStorage);
							} else {
								System.err.println("Risulta mandante la sezione identifier nel file ["+path.getAbsolutePath()+"]");
							}
						} else {
							System.err.println("Risulta mandante la sezione BIB nel file ["+path.getAbsolutePath()+"]");
						}
					} else {
						System.err.println("Riscontrato un problema nella lettura del file ["+path.getAbsolutePath()+"]");
					}
				} else {
					System.err.println("Il file ["+path.getAbsolutePath()+"] non e' raggiungibile");
				}
			}
		} catch (ConstraintViolationException e) {
			log.error(e.getMessage(), e);
//			filesStorageDAO.rollbackTransaction();
			throw e;
		} catch (HibernateException e) {
			log.error(e.getMessage(), e);
//			filesStorageDAO.rollbackTransaction();
			throw e;
		} catch (NamingException e) {
			log.error(e.getMessage(), e);
//			filesStorageDAO.rollbackTransaction();
			throw e;
		} catch (ConfigurationException e) {
			log.error(e.getMessage(), e);
//			filesStorageDAO.rollbackTransaction();
			throw e;
		} catch (XsdException e) {
			System.err.println("Riscontrato un problema con il file: ["+path.getAbsolutePath()+"] Err: "+e.getMessage());
//			log.error(e.getMessage(), e);
//			filesStorageDAO.rollbackTransaction();
//			throw e;
		}
	}
}
