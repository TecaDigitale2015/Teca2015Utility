/**
 * 
 */
package mx.teca2015.tecaUtility.tecaGenIIPImage;

import java.io.File;
import java.util.Iterator;
import java.util.Set;

import javax.naming.NamingException;

import org.hibernate.HibernateException;

import mx.randalf.configuration.exception.ConfigurationException;
import mx.randalf.hibernate.FactoryDAO;
import mx.randalf.hibernate.exception.HibernateUtilException;
import mx.teca2015.database.teca.entity.Tblimg;
import mx.teca2015.database.teca.entity.Tbllegnotris;
import mx.teca2015.database.teca.entity.Tblrelris;
import mx.teca2015.database.teca.entity.Tblris;

/**
 * @author massi
 *
 */
public class InfoImage {

	private File fileImgf = null;
	private String fileHibernate = null;
	private String piecedt = null;
	private String piecegr = null;
	private String piecein = null;
	private String bid = null;
	private String inventario = null;
	private String segnatura = null;

	/**
	 * @throws ConfigurationException 
	 * @throws NamingException 
	 * 
	 */
	public InfoImage(Tblimg tblimg, String fileHibernate ) throws HibernateException, HibernateUtilException {
		try {
			this.fileHibernate=fileHibernate;
			init(tblimg);
		} catch (HibernateException e) {
			throw e;
		} catch (HibernateUtilException e) {
			throw e;
		}
	}

	private void init(Tblimg tblimg) throws HibernateUtilException, HibernateException{
		
		try {
			if (tblimg.getTlkphost() != null) {
				FactoryDAO.initialize(tblimg.getTlkphost(), fileHibernate);
				fileImgf = new File(tblimg.getTlkphost().getHostserverpath());
				
				if (tblimg.getTlkphost().getHostpathdisco().startsWith("/")){
					fileImgf = new File(fileImgf.getAbsolutePath()+tblimg.getTlkphost().getHostpathdisco());
				} else {
					fileImgf = new File(fileImgf.getAbsolutePath()+File.separator+tblimg.getTlkphost().getHostpathdisco());
				}
	
				if (tblimg.getImgpathname().startsWith("/")){
					fileImgf = new File(fileImgf.getAbsolutePath()+tblimg.getImgpathname());
				} else if (tblimg.getImgpathname().startsWith("./")){
						fileImgf = new File(fileImgf.getAbsolutePath()+tblimg.getImgpathname().substring(1));
				} else {
					fileImgf = new File(fileImgf.getAbsolutePath()+File.separator+tblimg.getImgpathname());
				}
			} else {
				fileImgf = new File(tblimg.getImgpathname());
			}
			initTblris(tblimg.getTblrises());
		} catch (HibernateException e) {
			throw e;
		} catch (HibernateUtilException e) {
			throw e;
		}
	}

	private void initTblris(Set<Tblris> setTblris) throws HibernateException, HibernateUtilException {
		Tblris tblris = null;

		try {
			for (Iterator<Tblris> tblriss = setTblris.iterator(); tblriss.hasNext();) {
				tblris = (Tblris) tblriss.next();
				initTblrelris(tblris.getTblrelrisesForRelrisidrpartenza());
			}
		} catch (HibernateException e) {
			throw e;
		} catch (HibernateUtilException e) {
			throw e;
		}
	}

	private void initTblrelris(Set<Tblrelris> setTblrelris) throws HibernateException, HibernateUtilException {
		Tblrelris tblrelris = null;

		try {
			for (Iterator<Tblrelris> tblriss = setTblrelris.iterator(); tblriss.hasNext();) {
				tblrelris = (Tblrelris) tblriss.next();
				FactoryDAO.initialize(tblrelris.getTblrisByRelrisidrarrivo(), fileHibernate);
				initTbllegnotris(tblrelris.getTblrisByRelrisidrarrivo().getTbllegnotrises());
			}
		} catch (HibernateException e) {
			throw e;
		} catch (HibernateUtilException e) {
			throw e;
		}
	}

	private void initTbllegnotris(Set<Tbllegnotris> setTbllegnotris) throws HibernateException, HibernateUtilException {
		Tbllegnotris tbllegnotris = null;

		try {
			for (Iterator<Tbllegnotris> tblriss = setTbllegnotris.iterator(); tblriss.hasNext();) {
				tbllegnotris = (Tbllegnotris) tblriss.next();
				
				piecedt = tbllegnotris.getPiecedt();
				piecegr = tbllegnotris.getPiecegr();
				piecein = tbllegnotris.getPiecein();

				FactoryDAO.initialize(tbllegnotris.getTbllegnot(), fileHibernate);
				bid = tbllegnotris.getTbllegnot().getLegnotbid();
				inventario = tbllegnotris.getTbllegnot().getLegnotinv();
				segnatura = tbllegnotris.getTbllegnot().getLegnotsegna();
			}
		} catch (HibernateException e) {
			throw e;
		} catch (HibernateUtilException e) {
			throw e;
		}
		
	}

	public File getFileImgf() {
		return fileImgf;
	}

	public String getPiecedt() {
		return piecedt;
	}

	public String getPiecegr() {
		return piecegr;
	}

	public String getPiecein() {
		return piecein;
	}

	public String getBid() {
		return bid;
	}

	public String getInventario() {
		return inventario;
	}

	public String getSegnatura() {
		return segnatura;
	}
}
