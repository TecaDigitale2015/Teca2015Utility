/**
 * 
 */
package mx.teca2015.tecaUtility.tecaSync;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;

import mx.randalf.hibernate.FactoryDAO;
import mx.randalf.hibernate.exception.HibernateUtilException;
import mx.teca2015.database.teca.dao.ImggroupDAO;
import mx.teca2015.database.teca.dao.TblimgDAO;
import mx.teca2015.database.teca.dao.TbllegnotDAO;
import mx.teca2015.database.teca.dao.TbllegnotrisDAO;
import mx.teca2015.database.teca.dao.TbloaisetDAO;
import mx.teca2015.database.teca.dao.TblrelrisDAO;
import mx.teca2015.database.teca.dao.TblrisDAO;
import mx.teca2015.database.teca.dao.TblrisimgDAO;
import mx.teca2015.database.teca.dao.TlkphostDAO;
import mx.teca2015.database.teca.entity.Imggroup;
import mx.teca2015.database.teca.entity.Tblimg;
import mx.teca2015.database.teca.entity.Tbllegnot;
import mx.teca2015.database.teca.entity.Tbllegnotris;
import mx.teca2015.database.teca.entity.Tbloaiset;
import mx.teca2015.database.teca.entity.Tblrelris;
import mx.teca2015.database.teca.entity.Tblris;
import mx.teca2015.database.teca.entity.Tblrisimg;
import mx.teca2015.database.teca.entity.Tlkphost;

/**
 * @author massi
 *
 */
public class TecaSync {

	public static String HIBERNATETECAROMA = "hibernateTecaRoma.cfg.xml";
	private static Logger log = Logger.getLogger(TecaSync.class);

	/**
	 * 
	 */
	public TecaSync() {
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TecaSync tecaSync = null;
		int start =1;

		try {
			if (args.length==1){
				start = Integer.parseInt(args[0]);
			}
			tecaSync = new TecaSync();
			tecaSync.esegui(start);
		} catch (HibernateException e) {
			log.error(e.getMessage(), e);
		} catch (HibernateUtilException e) {
			log.error(e.getMessage(), e);
		}
	}

	public void esegui(int start) throws HibernateException, HibernateUtilException {
		TbllegnotDAO tbllegnotDAO = null;
		Tbllegnot tbllegnot = null;
		Integer maxId = null;

		try {
			maxId = maxId();
			tbllegnotDAO = new TbllegnotDAO(HIBERNATETECAROMA);
			for (int x = start; x <= maxId; x++) {
				if ((x % 10) == 0) {
					System.out.println("Record: " + x + "/" + maxId);
				}
				tbllegnot = tbllegnotDAO.getById(x);
				if (tbllegnot != null) {
					check(tbllegnot);
				}
			}
		} catch (HibernateException e) {
			throw e;
		} catch (HibernateUtilException e) {
			throw e;
		}
	}

	private void check(Tbllegnot tbllegnotOri) throws HibernateException, HibernateUtilException {
		TbllegnotDAO tbllegnotDAO = null;
		TbllegnotrisDAO tbllegnotrisDAO = null;
		Tbllegnot tbllegnot = null;
		List<Tbllegnotris> tbllegnotriss = null;

		try {
			tbllegnotDAO = new TbllegnotDAO();
			tbllegnot = tbllegnotDAO.getById(tbllegnotOri.getId());
			if (tbllegnot == null) {
//				System.out.println("Manca: "+tbllegnotOri.getId());
//				System.exit(0);
				tbllegnotDAO.save(tbllegnotOri);
			}

			tbllegnotrisDAO = new TbllegnotrisDAO(HIBERNATETECAROMA);
			tbllegnotriss = tbllegnotrisDAO.find(tbllegnotOri);
			for (Tbllegnotris tbllegnotris : tbllegnotriss) {
				check(tbllegnotris);
			}
		} catch (HibernateException e) {
			throw e;
		} catch (HibernateUtilException e) {
			throw e;
		}
	}

	private void check(Tbllegnotris tbllegnotrisOri) throws HibernateException, HibernateUtilException {
		TbllegnotrisDAO tbllegnotrisDAO = null;
		TblrisDAO tblrisDAO = null;
		List<Tbllegnotris> tbllegnotriss = null;

		try {
			tbllegnotrisDAO = new TbllegnotrisDAO();
			tbllegnotriss = tbllegnotrisDAO.find(tbllegnotrisOri.getTblris(), tbllegnotrisOri.getTbllegnot());
			if (tbllegnotriss == null || tbllegnotriss.size() == 0) {
//				System.out.println("Manca Tblris: " + tbllegnotrisOri.getTblris() + "Tbllegnot: "
//						+ tbllegnotrisOri.getTbllegnot());
//				System.exit(0);
				FactoryDAO.initialize(tbllegnotrisOri.getTlkphost(), HIBERNATETECAROMA);
				check(tbllegnotrisOri.getTlkphost());
				FactoryDAO.initialize(tbllegnotrisOri.getTbloaiset(), HIBERNATETECAROMA);
				check(tbllegnotrisOri.getTbloaiset());

				tblrisDAO = new TblrisDAO(HIBERNATETECAROMA);
				check(tblrisDAO.getById(tbllegnotrisOri.getTblris()));
				tbllegnotrisDAO.save(tbllegnotrisOri);
			}
		} catch (HibernateException e) {
			throw e;
		} catch (HibernateUtilException e) {
			throw e;
		}
	}

	private void check(Tbloaiset tbloaisetOri) throws HibernateException, HibernateUtilException {
		TbloaisetDAO tbloaisetDAO = null;
		Tbloaiset tbloaiset = null;

		try {
			tbloaisetDAO = new TbloaisetDAO();
			tbloaiset = tbloaisetDAO.findById(tbloaisetOri.getId());
			if (tbloaiset == null){
				tbloaisetDAO.save(tbloaisetOri);
			}
		} catch (HibernateException e) {
			throw e;
		} catch (HibernateUtilException e) {
			throw e;
		}
		
	}

	private void check(Tblris tblrisOri) throws HibernateException, HibernateUtilException {
		TblrisDAO tblrisDAO = null;
		TblrelrisDAO tblrelrisDAO = null;
		TblrisimgDAO tblrisimgDAO = null;
		Tblris tblris = null;
		List<Tblrelris> tblrelriss = null;
		List<Tblrisimg> tblrisimgs = null;
		
		try {
			tblrisDAO = new TblrisDAO();
			tblris = tblrisDAO.getById(tblrisOri.getId());
			if (tblris == null) {
//				System.out.println("Manca: "+tblrisOri.getId());
//			System.exit(0);
				tblrisDAO.save(tblrisOri);
			}

			tblrelrisDAO = new TblrelrisDAO(HIBERNATETECAROMA);
			
			tblrelriss = tblrelrisDAO.findtByRelrisidrarrivo(tblrisOri);
			if (tblrelriss != null){
				for (Tblrelris tblrelris: tblrelriss){
					checkArrivo(tblrelris);
				}
			}
			
//			tblrelriss = tblrelrisDAO.findtByRelrisidrpartenza(tblrisOri);
//			if (tblrelriss != null){
//				for (Tblrelris tblrelris: tblrelriss){
//					checkPartenza(tblrelris);
//				}
//			}

			tblrisimgDAO = new TblrisimgDAO(HIBERNATETECAROMA);
			tblrisimgs = tblrisimgDAO.find(tblrisOri);
			if (tblrisimgs != null){
				for (Tblrisimg tblrisimg : tblrisimgs){
					check(tblrisimg);
				}
			}
		} catch (HibernateException e) {
			throw e;
		} catch (HibernateUtilException e) {
			throw e;
		}
	}

	private void check(Tblrisimg tblrisimgOri) throws HibernateException, HibernateUtilException {
		TblrisimgDAO tblrisimgDAO = null;
		TblimgDAO tblimgDAO = null;
		List<Tblrisimg> tblrisimgs = null;
		
		try {
			tblrisimgDAO = new TblrisimgDAO();
			tblrisimgs = tblrisimgDAO.find(tblrisimgOri.getRisidr(), tblrisimgOri.getIdTblimg());
			if (tblrisimgs ==null || tblrisimgs.size()==0){
				tblimgDAO = new TblimgDAO(HIBERNATETECAROMA);
				check(tblimgDAO.findById(tblrisimgOri.getIdTblimg()));
				tblrisimgDAO.save(tblrisimgOri);
			}
		} catch (HibernateException e) {
			throw e;
		} catch (HibernateUtilException e) {
			throw e;
		}
	}

	private void check(Tblimg tblimgOri) throws HibernateException, HibernateUtilException {
		TblimgDAO tblimgDAO = null;
		Tblimg tblimg = null;

		try {
			tblimgDAO = new TblimgDAO();
			tblimg = tblimgDAO.findById(tblimgOri.getId());
			if (tblimg == null){
				FactoryDAO.initialize(tblimgOri.getImggroup(), HIBERNATETECAROMA);
				check(tblimgOri.getImggroup());
				FactoryDAO.initialize(tblimgOri.getTlkphost(), HIBERNATETECAROMA);
				check(tblimgOri.getTlkphost());
				tblimgDAO.save(tblimgOri);
			}
		} catch (HibernateException e) {
			throw e;
		} catch (HibernateUtilException e) {
			throw e;
		}
	}

	private void check(Tlkphost tlkphostOri) throws HibernateException, HibernateUtilException {
		TlkphostDAO tlkphostDAO = null;
		Tlkphost tlkphost = null;

		try {
			tlkphostDAO = new TlkphostDAO();
			tlkphost = tlkphostDAO.findById(tlkphostOri.getId());
			if (tlkphost== null){
				tlkphostDAO.save(tlkphostOri);
			}
		} catch (HibernateException e) {
			throw e;
		} catch (HibernateUtilException e) {
			throw e;
		}
	}

	private void check(Imggroup imggroupOri) throws HibernateException, HibernateUtilException {
		ImggroupDAO imggroupDAO = null;
		Imggroup imggroup = null;
		
		try {
			imggroupDAO = new ImggroupDAO();
			imggroup = imggroupDAO.findById(imggroupOri.getId());
			if(imggroup== null){
				imggroupDAO.save(imggroupOri);
			}
		} catch (HibernateException e) {
			throw e;
		} catch (HibernateUtilException e) {
			throw e;
		}
	}

	private void checkPartenza(Tblrelris tblrelrisOri) throws HibernateException, HibernateUtilException {
		TblrelrisDAO tblrelrisDAO = null;
		TblrisDAO tblrisDAO = null;
		List<Tblrelris> tblrelriss = null;
 
		try {
			tblrelrisDAO = new TblrelrisDAO();
			tblrelriss = tblrelrisDAO.find(tblrelrisOri.getTblrisByRelrisidrpartenza(), 
					tblrelrisOri.getTblrisByRelrisidrarrivo());
			if (tblrelriss == null || tblrelriss.size()==0){
				tblrisDAO = new TblrisDAO(HIBERNATETECAROMA);
				check(tblrisDAO.findById(tblrelrisOri.getTblrisByRelrisidrarrivo()));
				tblrelrisDAO.save(tblrelrisOri);
			}
		} catch (HibernateException e) {
			throw e;
		} catch (HibernateUtilException e) {
			throw e;
		}
	}

	private void checkArrivo(Tblrelris tblrelrisOri) throws HibernateException, HibernateUtilException {
		TblrelrisDAO tblrelrisDAO = null;
		TblrisDAO tblrisDAO = null;
		List<Tblrelris> tblrelriss = null;
 
		try {
			tblrelrisDAO = new TblrelrisDAO();
			FactoryDAO.initialize(tblrelrisOri.getTblrisByRelrisidrpartenza(), HIBERNATETECAROMA);
			FactoryDAO.initialize(tblrelrisOri.getTblrisByRelrisidrarrivo(), HIBERNATETECAROMA);
			tblrelriss = tblrelrisDAO.find(tblrelrisOri.getTblrisByRelrisidrpartenza(), 
					tblrelrisOri.getTblrisByRelrisidrarrivo());
			if (tblrelriss == null || tblrelriss.size()==0){
				tblrisDAO = new TblrisDAO(HIBERNATETECAROMA);
				check(tblrisDAO.findById(tblrelrisOri.getTblrisByRelrisidrpartenza()));
				tblrelrisDAO.save(tblrelrisOri);
			}
		} catch (HibernateException e) {
			throw e;
		} catch (HibernateUtilException e) {
			throw e;
		}
	}

	private Integer maxId() throws HibernateException, HibernateUtilException {
		TbllegnotDAO tbllegnotDAO = null;
		Integer result = 0;

		try {
			tbllegnotDAO = new TbllegnotDAO(HIBERNATETECAROMA);
			result = tbllegnotDAO.max("id");
			if (result == null) {
				result = 0;
			}
		} catch (HibernateException e) {
			throw e;
		} catch (HibernateUtilException e) {
			throw e;
		}
		return result;
	}
}
