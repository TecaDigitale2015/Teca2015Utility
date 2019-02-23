/**
 * 
 */
package mx.teca2015.tecaUtility.tecaRipristinoXlimage;

import java.util.List;

import org.hibernate.HibernateException;

import mx.randalf.hibernate.exception.HibernateUtilException;
import mx.teca2015.database.teca.dao.TblimgDAO;
import mx.teca2015.database.teca.entity.Tblimg;

/**
 * @author massi
 *
 */
public class TecaRipristinoXlimage {

	/**
	 * 
	 */
	public TecaRipristinoXlimage() {
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TecaRipristinoXlimage tecaRipristinoXlimage = null;
		
		tecaRipristinoXlimage = new TecaRipristinoXlimage();
		tecaRipristinoXlimage.esegui();
		
	}

	public void esegui() {
		TblimgDAO tblimgDAO = null;
		List<Tblimg> tblimgs = null;
		String fileName = null;

		try {
			tblimgDAO = new TblimgDAO();
			tblimgs = tblimgDAO.findUsage("5");
			for (Tblimg tblimg: tblimgs) {
				tblimg.setImgusage("6");
				fileName  = tblimg.getImgpathname();
				fileName = fileName.replace(".imgf",".tif");
				tblimg.setImgpathname(fileName);
				tblimgDAO.update(tblimg);
			}
		} catch (HibernateException e) {
			e.printStackTrace();
		} catch (HibernateUtilException e) {
			e.printStackTrace();
		}
	}

}
