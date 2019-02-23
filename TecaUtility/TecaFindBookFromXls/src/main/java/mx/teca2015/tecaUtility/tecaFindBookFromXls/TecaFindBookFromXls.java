/**
 * 
 */
package mx.teca2015.tecaUtility.tecaFindBookFromXls;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;

import mx.randalf.hibernate.exception.HibernateUtilException;
import mx.teca2015.database.teca.dao.TbllegnotDAO;
import mx.teca2015.database.teca.entity.Tbllegnot;

/**
 * @author massi
 *
 */
public class TecaFindBookFromXls {

	private Logger log = Logger.getLogger(TecaFindBookFromXls.class);

	public static String HIBERNATETECAROMA = "hibernateTecaRoma.cfg.xml";

	/** Application name. */
	private static final String APPLICATION_NAME = "Google Sheets API Java Quickstart";

	/** Directory to store user credentials for this application. */
	private static final java.io.File DATA_STORE_DIR = new java.io.File(System.getProperty("user.home"),
			".credentials/sheets.googleapis.com-java-quickstart");

	/** Global instance of the {@link FileDataStoreFactory}. */
	private static FileDataStoreFactory DATA_STORE_FACTORY;

	/** Global instance of the JSON factory. */
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

	/** Global instance of the HTTP transport. */
	private static HttpTransport HTTP_TRANSPORT;

	/**
	 * Global instance of the scopes required by this quickstart.
	 *
	 * If modifying these scopes, delete your previously saved credentials at
	 * ~/.credentials/sheets.googleapis.com-java-quickstart
	 */
	private static final List<String> SCOPES = Arrays.asList(SheetsScopes.SPREADSHEETS_READONLY);

	static {
		try {
			HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
			DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
		} catch (Throwable t) {
			t.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * 
	 */
	public TecaFindBookFromXls() {
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TecaFindBookFromXls tecaFindBookFromXls = null;
		
		try {
			tecaFindBookFromXls = new TecaFindBookFromXls();
			
			tecaFindBookFromXls.esegui(args[0], args[1]);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (HibernateUtilException e) {
			e.printStackTrace();
		}
	}

	public void esegui(String clientSecret, String spreadsheetId) throws IOException, HibernateUtilException {
		Sheets service = null;
		List<List<Object>> rows = null;

		try {
			// Apro la connessione con il documento.
			service = getSheetsService(new File(clientSecret));

			rows = readRows(service, spreadsheetId, "'Già Digitalizzato'!F2:F");
			analizza("GiàDigitalizzato", rows);

			rows = readRows(service, spreadsheetId, "'Da Digitalizzare'!E2:E");
			analizza("DaDigitalizzato", rows);
		} catch (IOException e) {
			throw e;
		} catch (HibernateUtilException e) {
			throw e;
		}
	}

	private void analizza(String etichetta, List<List<Object>> rows) throws HibernateUtilException {
		TbllegnotDAO tbllegnotDAO = null;
		List<Tbllegnot> tbllegnots = null;
		int rowTot = 0;
		int rowTrov = 0;
		int rowNonTrov = 0;

		try {
			System.out.println(etichetta);
			tbllegnotDAO = new TbllegnotDAO(HIBERNATETECAROMA);
			for (List<Object> row: rows) {
				rowTot++;
				tbllegnots = tbllegnotDAO.find((String) row.get(0));
				if (tbllegnots == null || tbllegnots.size()==0) {
					rowNonTrov++;
				} else {
					rowTrov++;
					System.out.println("\t"+((String)row.get(0)).trim());
				}
			}
			System.out.println("\t Tot: "+rowTot+" \t Trovati: "+rowTrov+" \t Non Trovati: "+rowNonTrov);
		} catch (HibernateException e) {
			throw e;
		} catch (HibernateUtilException e) {
			throw e;
		}
	}

	private List<List<Object>> readRows(Sheets service, String spreadsheetId, String range) throws IOException {
		List<List<Object>> rows = null;
		ValueRange response = null;

		try {
			response = service.spreadsheets().values().get(spreadsheetId, range).execute();
			rows = response.getValues();
		} catch (IOException e) {
			throw e;
		}
		return rows;
	}
	
	/**
	 * Creates an authorized Credential object.
	 * 
	 * @return an authorized Credential object.
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private Credential authorize(File clientSecret) throws FileNotFoundException, IOException {
		// Load client secrets.
		InputStream in = null;
		GoogleClientSecrets clientSecrets = null;
		GoogleAuthorizationCodeFlow flow = null;
		Credential credential = null;

		try {
			in = new FileInputStream(clientSecret);
			// in =ReadGoogle.class.getResourceAsStream("/client_secret.json");
			clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

			// Build flow and trigger user authorization request.
			flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
					.setDataStoreFactory(DATA_STORE_FACTORY).setAccessType("offline").build();
			credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
			log.debug("Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
		} catch (FileNotFoundException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				throw e;
			}
		}
		return credential;
	}

	/**
	 * Build and return an authorized Sheets API client service.
	 * 
	 * @return an authorized Sheets API client service
	 * @throws IOException
	 */
	private Sheets getSheetsService(File clientSecret) throws IOException {
		Credential credential = authorize(clientSecret);
		return new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME)
				.build();
	}
}
