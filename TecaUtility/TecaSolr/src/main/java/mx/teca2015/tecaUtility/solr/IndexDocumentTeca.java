/**
 * 
 */
package mx.teca2015.tecaUtility.solr;

import it.bncf.magazziniDigitali.solr.IndexDocumentMD;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;

import mx.randalf.solr.exception.SolrException;

/**
 * @author massi
 *
 */
public class IndexDocumentTeca extends IndexDocumentMD {

	/**
	 * 
	 */
	public IndexDocumentTeca() {
		super();
	}

	/**
	 * @param fileName
	 */
	public IndexDocumentTeca(String fileName) {
		super(fileName);
	}

	public void publish(String batchSolr, File fSolr) throws SolrException {
		Runtime runtime = null;
		String[] cmdarray = null;
		Process process = null;
		Integer processResult = null;
		String line = null; 
		BufferedReader stdErr = null;
		Vector<String> err = new Vector<String>();
		BufferedReader stdOut = null;
		Vector<String> out = new Vector<String>();
		boolean error = false;
		String cmd = null;

		try {
			if (fSolr.length()>0){
				runtime = Runtime.getRuntime();
	
				cmdarray = new String[2];
				cmdarray[0] = batchSolr;
				cmdarray[1] = fSolr.getAbsolutePath().replace(" ", "\\ ");
				cmd = cmdarray[0]+" "+cmdarray[1];
				process = runtime.exec(cmd);
				stdErr = new BufferedReader(new InputStreamReader(
						process.getErrorStream()));
				stdOut = new BufferedReader(new InputStreamReader(
						process.getInputStream()));
	
				while ((line = stdOut.readLine()) != null) {
					if (line.toLowerCase().indexOf("fatal")>-1 ||
							line.toLowerCase().indexOf("error")>-1 ||
							line.toLowerCase().indexOf("warning")>-1
							){
						error = true;
					}
					out.add(line);
				}
	
				while ((line = stdErr.readLine()) != null) {
					error = true;
					err.add(line);
				}
	
				processResult = process.waitFor();
				if (processResult !=0){
					error= true;
				}
				printFile(fSolr, error, err, out);
			}
		} catch (IOException e) {
			throw new SolrException(e.getMessage(), e);
		} catch (InterruptedException e) {
			throw new SolrException(e.getMessage(), e);
		}

	}

	private void printFile(File fSolr, boolean error, Vector<String> err, Vector<String> out) throws IOException{
		File fOut = null;
		BufferedWriter bw = null;
		FileWriter fw = null;
		
		try {
			fOut = new File(fSolr.getAbsolutePath().replace(".XML.solr", ".xml")
					.replace(".xml.solr", ".xml")+(error?".elabKO":".elabOK"));
			fw = new FileWriter(fOut);
			bw = new  BufferedWriter(fw);

			if (out != null &&
					out.size()>0){
				for (int x=0; x<out.size(); x++){
					bw.write(out.get(x)+"\n");
				}
			}

			if (err != null &&
					err.size()>0){
				for (int x=0; x<err.size(); x++){
					bw.write(err.get(x)+"\n");
				}
			}
		} catch (IOException e) {
			throw e;
		} finally {
			try {
				if (bw != null){
					bw.flush();
					bw.close();
				}
				if (fw != null){
					fw.close();
				}
			} catch (IOException e) {
				throw e;
			}
		}
	}

	public void optimize() {
		// /Users/massi/Desktop/Lavoro/Sorgenti/Teca2015/Teca2015Publisher/TecaPublished/TecaPublishStandAlone/solr-5.2.1/bin/post
		// -c tecaSvil -host 192.168.233.100 -out yes -d $'<optimize
		// waitSearcher="false"/>'
	}
}
