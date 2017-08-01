/**
 * 
 */
package mx.teca2015.tecaUtility.tecaGenIIPImage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.naming.NamingException;
import javax.xml.datatype.DatatypeConfigurationException;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.exception.ConstraintViolationException;
import org.im4java.core.InfoException;
import org.im4java.process.ProcessStarter;

import mx.randalf.converter.IIPImage.RandalfConverterIIPImage;
import mx.randalf.converter.IIPImage.exception.RandalfConverterIIPImageException;
import mx.randalf.digest.MD5;
import mx.randalf.digital.img.reader.CalcImg;
import mx.randalf.hibernate.FactoryDAO;
import mx.randalf.hibernate.exception.HibernateUtilException;
import mx.teca2015.database.teca.dao.ImggroupDAO;
import mx.teca2015.database.teca.dao.TblimgDAO;
import mx.teca2015.database.teca.dao.TlkphostDAO;
import mx.teca2015.database.teca.dao.TlkpmimeDAO;
import mx.teca2015.database.teca.entity.Tblimg;

/**
 * @author massi
 *
 */
public class TecaGenIIPImage {

	private static Logger log = Logger.getLogger(TecaGenIIPImage.class);
	
	private String fileHibernate = null;

	private int nonTravoto = 0;
	private int trovato = 0;
	private int incerto = 0;

	private int totNonTravoto = 0;
	private int totTrovato = 0;
	private int totIncerto = 0;
	private int totale = 0;

	private String pathImageMagick = null;

	private boolean elabora = true;

//
//	private String pathTmp = null;

	/**
	 * 
	 */
	public TecaGenIIPImage(String pathImageMagick) {
		this.pathImageMagick = pathImageMagick;
		ProcessStarter.setGlobalSearchPath(pathImageMagick);
//		this.pathTmp = pathTmp;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TecaGenIIPImage tgii = null;

		
		try {
			if (args.length==1){
				tgii = new TecaGenIIPImage(args[0]);
				tgii.esegui();
			} else {
				System.out.println("Specificare i seguenti parametri:");
				System.out.println("1) path file imagemagick");
			}
		} catch (NamingException e) {
			log.error(e.getMessage(), e);
		} catch (HibernateUtilException e) {
			log.error(e.getMessage(), e);
		} catch (RandalfConverterIIPImageException e) {
			log.error(e.getMessage(), e);
		} catch (InfoException e) {
			log.error(e.getMessage(), e);
		}
	}

	public void esegui() throws NamingException, HibernateUtilException, RandalfConverterIIPImageException, InfoException {
		int page = 1;
		
		while(elabora){
			if ((totale%10)==0 && totale>0){
				System.out.println(totale +"\t Trovati: "+totTrovato+"\t Non Trovati: "+totNonTravoto+"\t Incerti: "+totIncerto);
			}
			if (!esegui(page)){
				page++;
			}
			totale++;
		}
		System.out.println(totale +"\t Trovati: "+totTrovato+"\t Non Trovati: "+totNonTravoto+"\t Incerti: "+totIncerto);
	}

	public boolean esegui(int page) throws NamingException, HibernateUtilException, RandalfConverterIIPImageException, InfoException {
		TblimgDAO tblimgDAO = null;
		List<Tblimg> lista = null;
		boolean autoTransaction = false;
		InfoImage infoImage = null;
		boolean result = true;

		try {
			tblimgDAO = new TblimgDAO();
			tblimgDAO.setPage(page);
			tblimgDAO.setPageSize(1);
			this.fileHibernate = tblimgDAO.getFileHibernate();
			autoTransaction = FactoryDAO.beginTransaction(fileHibernate);
			lista = tblimgDAO.findUsage("5");
			if (lista.size()>0){
				nonTravoto = 0;
				trovato = 0;
				incerto = 0;

				for (int x = 0; x < lista.size(); x++) {
					infoImage = new InfoImage(lista.get(x), fileHibernate);
					checkStorage(infoImage, lista.get(x));
				}
				if (nonTravoto>0 || incerto>0){
					result = false;
				}
				totNonTravoto += nonTravoto;
				totIncerto += incerto;
				totTrovato += trovato;
			} else {
				elabora =false;
			}
			FactoryDAO.commitTransaction(autoTransaction);
		} catch (HibernateException e) {
			FactoryDAO.rollbackTransaction(autoTransaction);
			log.error(e.getMessage(), e);
		} catch (HibernateUtilException e) {
			FactoryDAO.rollbackTransaction(autoTransaction);
			log.error(e.getMessage(), e);
		} catch (InfoException e) {
			FactoryDAO.rollbackTransaction(autoTransaction);
//			log.error(e.getMessage(), e);
			throw e;
		} catch (NoSuchAlgorithmException e) {
			FactoryDAO.rollbackTransaction(autoTransaction);
			log.error(e.getMessage(), e);
		} catch (FileNotFoundException e) {
			FactoryDAO.rollbackTransaction(autoTransaction);
			log.error(e.getMessage(), e);
		} catch (DatatypeConfigurationException e) {
			FactoryDAO.rollbackTransaction(autoTransaction);
			log.error(e.getMessage(), e);
		} catch (IOException e) {
			FactoryDAO.rollbackTransaction(autoTransaction);
			log.error(e.getMessage(), e);
		}
		return result;
	}

	private void checkStorage(InfoImage infoImage, Tblimg tblimg) throws HibernateException, HibernateUtilException,
				ConstraintViolationException, InfoException,
				NoSuchAlgorithmException, FileNotFoundException, DatatypeConfigurationException, 
				IOException, RandalfConverterIIPImageException {
		File fInput = null;
		File fOutput = null;

		try {
			if (infoImage.getFileImgf().exists()){
//			/mnt/STORAGE/TECA/san5/d2/bu/
				fInput = new File(infoImage.
						getFileImgf().
						getAbsolutePath().
						replace("/mnt/Storage/STORAGE01/","/mnt/STORAGE/TECA/san5/").replace(".imgf", ".tif"));
				if (!fInput.exists()){
					fInput = new File(fInput.getParentFile().getAbsolutePath()+
							File.separator+
							fInput.getName().replace(".tif", ".TIF"));
					if (!fInput.exists()){
						fInput = new File(fInput.getParentFile().getAbsolutePath()+
								File.separator+
								"archivio"+
								File.separator+
								fInput.getName());
						if (!fInput.exists()){
							fInput = new File(fInput.getParentFile().getAbsolutePath()+
									File.separator+
									fInput.getName().replace(".TIF", ".tif"));
						}
					}
				}
				if (fInput.exists()){
					fOutput = new File(infoImage.
							getFileImgf().
							getAbsolutePath().
							replace("/mnt/Storage/","/mnt/Storage/IIPIMAGE/").replace(".imgf", ".tif"));

					System.out.println("File: "+infoImage.getFileImgf()+" - "+infoImage.getFileImgf().exists());
					System.out.println("-> "+fInput.getAbsolutePath()+" - "+fInput.exists());
					System.out.println("---> "+fOutput.getAbsolutePath()+" - "+fOutput.exists());
					convert(fInput, fOutput, tblimg);
				} else {
					nonTravoto ++;
				}
			} else {
				nonTravoto ++;
			}
		} catch (ConstraintViolationException e) {
			throw e;
		} catch (HibernateException e) {
			throw e;
		} catch (InfoException e) {
			throw e;
		} catch (NoSuchAlgorithmException e) {
			throw e;
		} catch (FileNotFoundException e) {
			throw e;
		} catch (DatatypeConfigurationException e) {
			throw e;
		} catch (HibernateUtilException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		} catch (RandalfConverterIIPImageException e) {
			throw e;
		}
	}

//	private void checkStorage2(InfoImage infoImage, Tblimg tblimg) throws HibernateException, NamingException, ConfigurationException,
//				ConstraintViolationException, InfoException,
//				NoSuchAlgorithmException, FileNotFoundException, DatatypeConfigurationException, 
//				IOException, RandalfConverterIIPImageException{
//		FilesStorageDAO filesStorageDAO = null;
//		List<FilesStorage> filesStorages = null;
//		File fMag = null;
//		File fMagNew = null;
//		
//		try {
//			filesStorageDAO = new FilesStorageDAO(null);
//			filesStorages = filesStorageDAO.find(infoImage.getBid(), 
//					infoImage.getPiecedt(), 
//					infoImage.getPiecegr(), 
//					infoImage.getPiecein(), 
//					infoImage.getSegnatura(), 
//					infoImage.getInventario(),
//					new String[] {"/publicato/", "-IMGF.xml", "-new.xml"});
//
//			if (filesStorages.size() >1){
//				incerto ++;
////				System.out.println("\t [INCERTO]");
////				for (int x=0; x<filesStorages.size(); x++){
////					System.out.println("\t\t\t fileImgf: "+infoImage.getFileImgf()+
////							"\t fileMag: "+filesStorages.get(x).getPath());
////				}
//			} else if (filesStorages.size() ==0){
//				nonTravoto ++;
////				System.out.println("\t [NON PRESENTE]"+
////						"\t Bid: "+infoImage.getBid()+
////						"\t Inventario: "+infoImage.getInventario()+
////						"\t Segnatura: "+infoImage.getSegnatura()+
////						"\t Piecedt: "+infoImage.getPiecedt()+
////						"\t Piecegr: "+infoImage.getPiecegr()+
////						"\t Piecein: "+infoImage.getPiecein()+
////						"\t fileImgf: "+infoImage.getFileImgf());
//			} else {
//				fMag = new File(filesStorages.get(0).getPath());
//				if (fMag.exists()){
//					fMagNew = new File(fMag.getParentFile().getAbsolutePath()+
//							File.separator+
//							fMag.getName().replace(".xml", "-new.xml"));
//					if (fMagNew.exists()){
//						analizza(fMagNew, infoImage.getFileImgf(), tblimg);
//					} else {
//						analizza(fMag, infoImage.getFileImgf(), tblimg);
//					}
//				} else {
//					nonTravoto ++;
//					System.err.println("File non presente ["+fMag.getAbsolutePath()+"]");
//				}
////				System.out.println("\t [PRESENTE]"+
////						"\t fileImgf: "+infoImage.getFileImgf());
////				System.out.println("\t\t\t fileMag: "+filesStorages.get(0).getPath());
//			}
//		} catch (HibernateException e) {
//			throw e;
//		} catch (NamingException e) {
//			throw e;
//		} catch (ConfigurationException e) {
//			throw e;
//		}
//	}

//	private void analizza(File fMag, File fImgf, Tblimg tblimg)  throws ConstraintViolationException, HibernateException, InfoException,
//			NoSuchAlgorithmException, FileNotFoundException, DatatypeConfigurationException, NamingException, 
//			ConfigurationException, IOException, RandalfConverterIIPImageException{
//		MagXsd magXsd = null;
//		Metadigit mag = null;
//		File fImg = null;
//		File fIIPImage = null;
//
//		try {
//			magXsd = new MagXsd();
//			mag = magXsd.read(fMag);
//
//			if (mag.getImg() != null && mag.getImg().size()>0){
//				for (int x=0; x<mag.getImg().size(); x++){
//					if (((mag.getImg().get(x).getUsage() == null ||
//							mag.getImg().get(x).getUsage().size()==0) && 
//							mag.getImg().get(x).getFile().getHref().endsWith(".tif")) ||
//							(mag.getImg().get(x).getUsage() != null && 
//							mag.getImg().get(x).getUsage().size()>0 &&
//							mag.getImg().get(x).getUsage().get(0).equals("1"))){
//						fImg = new File(fMag.getParentFile().getAbsolutePath()+
//								File.separator+
//								mag.getImg().get(x).getFile().getHref());
//					} else if (mag.getImg().get(x).getAltimg() != null &&
//							mag.getImg().get(x).getAltimg().size()>0){
//						for (int y=0; y<mag.getImg().get(x).getAltimg().size(); y++){
//							if (((mag.getImg().get(x).getAltimg().get(y).getUsage() == null ||
//									mag.getImg().get(x).getAltimg().get(y).getUsage().size()==0) && 
//									mag.getImg().get(x).getAltimg().get(y).getFile().getHref().endsWith(".tif")) ||
//									(mag.getImg().get(x).getAltimg().get(y).getUsage() != null && 
//									mag.getImg().get(x).getAltimg().get(y).getUsage().size()>0 &&
//									mag.getImg().get(x).getAltimg().get(y).getUsage().get(0).equals("1"))){
//								fImg = new File(fMag.getParentFile().getAbsolutePath()+
//										File.separator+
//										mag.getImg().get(x).getAltimg().get(y).getFile().getHref());
//								break;
//							}
//						}
//					}
//					if (fImg != null){
//						if (fImg.getName().replace(".tif", "").equals(fImgf.getName().replace(".imgf", ""))){
//							break;
//						}
////					} else {
////						System.err.println("Nel file ["+fMag.getAbsolutePath()+"] non è stato indicato il formato Archivio per la posizione ["+x+"]");
//					}
//					fImg = null;
//				}
//				if (fImg != null){
//					fIIPImage = new File(fImgf.getAbsolutePath().replace("/mnt/Storage/","/mnt/Storage/IIPIMAGE/").replace(".imgf", ".tif"));
//					convert(fImg, fIIPImage, tblimg);
////					System.out.println(fImg.getAbsolutePath()+" -> "+fIIPImage.getAbsolutePath());
//				} else {
//					System.err.println("Nel file ["+fMag.getAbsolutePath()+"] non è stato trovato il file ["+fImgf.getAbsolutePath()+"]");
//					nonTravoto++;
//				}
//			} else {
//				System.err.println("Nel file ["+fMag.getAbsolutePath()+"] non sono state indicate le immagini");
//				nonTravoto++;
//			}
//		} catch (XsdException e) {
//			log.error("["+fMag.getAbsolutePath()+"] Errore ["+e.getMessage()+"]");
//		}
//	}

	private void convert(File fInput, File fOutput, Tblimg tblimg)  throws ConstraintViolationException, HibernateException, InfoException,
			NoSuchAlgorithmException, FileNotFoundException, DatatypeConfigurationException, HibernateUtilException, 
			IOException, RandalfConverterIIPImageException{
		RandalfConverterIIPImage tecaGenIIPImage = null;
		
		try {
			if (!fOutput.exists()){
				tecaGenIIPImage = new RandalfConverterIIPImage(pathImageMagick);
				
				if (!tecaGenIIPImage.convertImg(fInput, fOutput)){
					throw new RandalfConverterIIPImageException("Problemi nella conversione del file ["+fInput.getAbsolutePath()+"] in ["+fOutput.getAbsolutePath()+"]");
				}
			}
			updateImg(tblimg, fOutput);
		} catch (RandalfConverterIIPImageException e) {
			log.error("Problemi nella conversione del file ["+fInput.getAbsolutePath()+"] in ["+fOutput.getAbsolutePath()+"] Errore ["+e.getMessage()+"]");
			throw e;
		}
	}

	private void updateImg(Tblimg tblimg, File fOutput) throws ConstraintViolationException, HibernateException, InfoException,
				NoSuchAlgorithmException, FileNotFoundException, DatatypeConfigurationException, HibernateUtilException, 
				 IOException{
		ImggroupDAO imggroupDAO = null;
		TblimgDAO tblimgDAO = null;
		TlkpmimeDAO tlkpmimeDAO = null;
		TlkphostDAO tlkphostDAO = null;
		CalcImg calcImg = null;
		DecimalFormat df4 = new DecimalFormat("0000");
		DecimalFormat df2 = new DecimalFormat("00");
		String imgdata = null;
		MD5 md5 = null;
		GregorianCalendar gc = null;
		
		try {
			calcImg = new CalcImg(fOutput, true);
			
			if (calcImg.getDateTimeCreate() != null){
				imgdata = df4.format(calcImg.getDateTimeCreate().getYear());
				imgdata += df2.format(calcImg.getDateTimeCreate().getMonth());
				imgdata += df2.format(calcImg.getDateTimeCreate().getDay());
				imgdata += df2.format(calcImg.getDateTimeCreate().getHour());
				imgdata += df2.format(calcImg.getDateTimeCreate().getMinute());
				imgdata += df2.format(calcImg.getDateTimeCreate().getSecond());
			} else {
				gc = new GregorianCalendar();
				gc.setTimeInMillis(fOutput.lastModified());
				imgdata = df4.format(gc.get(Calendar.YEAR));
				imgdata += df2.format(gc.get(Calendar.MONTH)+1);
				imgdata += df2.format(gc.get(Calendar.DAY_OF_MONTH));
				imgdata += df2.format(gc.get(Calendar.HOUR_OF_DAY));
				imgdata += df2.format(gc.get(Calendar.MINUTE));
				imgdata += df2.format(gc.get(Calendar.SECOND));
			}
			
			tblimg.setImgdata(imgdata);
			tblimg.setImgformato("TIF");

			FactoryDAO.initialize(tblimg.getImggroup(), fileHibernate);
			imggroupDAO = new ImggroupDAO();
			if (tblimg.getImggroup().getIdImggroup()==259){
				tblimg.setImggroup(imggroupDAO.findById(317));
			} else {
				tblimg.setImggroup(imggroupDAO.findById(318));
			}
			
			tblimg.setImglength(calcImg.getImageLength().intValue());
			tblimg.setImglengthConv(calcImg.getImageLength().intValue());

			md5 = new MD5();
			tblimg.setImgmd5(md5.getDigest(fOutput));
			tblimg.setImgpathname(fOutput.getAbsolutePath().replace("/mnt/Storage/IIPIMAGE", "."));
			tblimg.setImgsize(new Long(fOutput.length()).intValue());
			tblimg.setImgusage("6");
			tblimg.setImgwidth(calcImg.getImageWidth().intValue());
			tblimg.setImgwidthConv(calcImg.getImageWidth().intValue());

			tlkphostDAO = new TlkphostDAO();
			tblimg.setTlkphost(tlkphostDAO.findById(565));

			tlkpmimeDAO = new TlkpmimeDAO();
			tblimg.setTlkpmime(tlkpmimeDAO.findById(2));
			
			tblimgDAO = new TblimgDAO();
			tblimgDAO.update(tblimg);
			trovato++;
		} catch (ConstraintViolationException e) {
			throw e;
		} catch (HibernateException e) {
			throw e;
		} catch (InfoException e) {
			throw e;
		} catch (NoSuchAlgorithmException e) {
			throw e;
		} catch (FileNotFoundException e) {
			throw e;
		} catch (DatatypeConfigurationException e) {
			throw e;
		} catch (HibernateUtilException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		} catch (NullPointerException e) {
			throw e;
		}
		
	}
}
