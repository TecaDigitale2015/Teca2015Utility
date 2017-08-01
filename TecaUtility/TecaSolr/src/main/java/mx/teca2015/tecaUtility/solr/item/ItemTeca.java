/**
 * 
 */
package mx.teca2015.tecaUtility.solr.item;

import it.bncf.magazziniDigitali.solr.ItemMD;

/**
 * @author massi
 *
 */
public class ItemTeca extends ItemMD {

	/**
	 * Campo relativo all'oaiset dell'opera che verrà utilizzato per le ricerche
	 */
	public static String TIPOLOGIAFILE="tipologiaFile";
	public static String TIPOLOGIAFILE_MAG="Mag";
	public static String TIPOLOGIAFILE_UC="Uc";
	public static String TIPOLOGIAFILE_UD="Ud";

	public static String TIPOLOGIAFILE_BUSTE = "Buste";
	public static String TIPOOGGETTO_BUSTE = "Buste";

	public static String TIPOLOGIAFILE_SCHEDAF="SchedaF";
	public static String TIPOLOGIAFILE_SOGGETTOCONSERVATORE="SoggettoConservatore";
	public static String TIPOLOGIAFILE_COMPLESSOARCHIVISTICO="ComplessoArchivistico";
	public static String TIPOLOGIAFILE_SOGGETTOPRODUTTORE="SoggettoProduttore";

	public static String TIPOOGGETTO_SCHEDAF="SchedaF";
	public static String TIPOOGGETTO_COLLANA="collana";
	public static String TIPOOGGETTO_SOGGETTOCONSERVATORE="SoggettoConservatore";
	public static String TIPOOGGETTO_COMPLESSOARCHIVISTICO="ComplessoArchivistico";
	public static String TIPOOGGETTO_SOGGETTOPRODUTTORE="SoggettoProduttore";
	
	public static String XML= "xml";
	
	public static String SOGGETTOCONSERVATORE="soggettoConservatore";
	public static String SOGGETTOCONSERVATOREKEY="soggettoConservatoreKey";
	public static String SOGGETTOCONSERVATORESCHEDA="soggettoConservatoreScheda";
	public static String FONDO="fondo";
	public static String FONDOKEY="fondoKey";
	public static String FONDOSCHEDA="fondoScheda";
	public static String SUBFONDO="subFondo";
	public static String SUBFONDOKEY="subFondoKey";
	public static String SUBFONDOSCHEDA="subFondoScheda";
	public static String SUBFONDO2="subFondo2";
	public static String SUBFONDO2KEY="subFondo2Key";
	public static String SUBFONDO2SCHEDA="subFondo2Scheda";
	public static String _ROOTDESC_="_rootDesc_";
	public static String TIPOLOGIAMATERIALE="tipologiaMateriale";
	public static String DATACRONICA="dataCronica";
	public static String DATATOPICA="dataTopica";
	public static String SUPPORTO="supporto";
	public static String TECNICHE="tecniche";
	public static String DIMENSIONE="dimensione";
	public static String SCALA="scala";
	public static String STATOCONSERVAZIONE="statoConservazione";
	public static String DATIFRUIZIONE="datiFruizione";
	public static String COMPILATORE="compilatore";
	public static String DATACOMPILAZIONE="dataCompilazione";
	public static String NOTE="note";
	public static String TIPOLOGIAUNITAARCHIVISTICA="tipologiaUnitaArchivistica";
	public static String ANNOINIZIALE="annoIniziale";
	public static String ANNOFINALE="annoFinale";
	public static String SECOLOINIZIALE="secoloIniziale";
	public static String SECOLOFINALE="secoloFinale";
	public static String CONSISTENZACARTE="consistenzaCarte";
	public static String CONSISTENZASAST="consistenzaSast";
	public static String DOCUMENTICARTOGRAFICI="documentiCartografici";
	public static String CHILDREN="children";
	public static String CHILDRENDESC="childrenDesc";
	
	public static String XMLSCHEDAF= "xmlSchedaF";
	public static String ENTESCHEDATORE= "enteSchedatore";
	public static String ENTECOMPETENTE= "enteCompetente";
	public static String STATO= "stato";
	public static String REGIONE= "regione";
	public static String PROVINCIA= "provincia";
	public static String COMUNE= "comune";
	public static String DENOMINAZIONE= "denominazione";
	public static String DENOMINAZIONEINDIRIZZO= "denominazioneIndirizzo";
	public static String DENOMINAZIONERACCOLTA= "denominazioneRaccolta";
	public static String FONDOSPECIFICHE= "fondoSpecifiche";
	public static String REFERENTESCIENTIFICO= "referenteScientifico";
	public static String FUNZIONARIORESPONSABILE= "funzionarioResponsabile";

	public static String INDIRIZZO="indirizzo";
	public static String TELEFONO="telefono";
	public static String FAX="fax";

	public static String ESTREMI="estremi";
	public static String STORIAARCHIVISTICA="storiaArchivistica";
	public static String SOGGETTOPRODUTTOREKEY="soggettoProduttoreKey";
	public static String SOGGETTOPRODUTTORE="soggettoProduttore";

	public static String TIPOSOGGETTOCONSERVATORE="tipoSoggettoConservatore";
	public static String EMAIL="email";
	public static String SERVIZIOPUB="servizioPub";
	public static String ORARIOAPERTURA="orarioApertura";
	public static String SCHEDECONSERVATORI="schedeConservatori";
	public static String SCHEDECONSERVATORIURL="schedeConservatoriUrl";
	public static String RISORSEESTERNE="risorseEsterne";
	public static String RISORSEESTERNEURL="risorseEsterneUrl";

	public static String TIPOLOGIA="tipologia";
	public static String SISTEMAADERENTE="sistemaAderente";
	public static String SCHEDAPROVENIENZAURL="schedaProvenienzaUrl";
	
	public static String ALTREDENOMINAZIONI="altreDenominazioni";
	public static String DATAESISTENZA="dataEsistenza";
	public static String DATAMORTE="dataMorte";
	public static String LUOGONASCITA="luogoNascita";
	public static String LUOGOMORTE="luogoMorte";
	public static String SEDE="sede";
	public static String NATURAGIURIDICA="naturaGiuridica";
	public static String TIPOENTE="tipoEnte";
	public static String AMBITOTERRITORIALE="ambitoTerritoriale";
	public static String TITOLOSP="titoloSP";

	/**
	 * 
	 */
	public ItemTeca() {
		super();
		addColumn(TIPOLOGIAFILE, false, true, true, true, true);
		addColumn(XML, true, false, false, false, false);

		addColumn(SOGGETTOCONSERVATORE, true, true, true, true, true);
		addColumn(SOGGETTOCONSERVATOREKEY, true, true, true, true, true);
		addColumn(SOGGETTOCONSERVATORESCHEDA, true, true, true, true, true);
		addColumn(FONDO, true, true, true, true, true);
		addColumn(FONDOKEY, true, true, true, true, true);
		addColumn(FONDOSCHEDA, true, true, true, true, true);
		addColumn(SUBFONDO, true, true, true, true, true);
		addColumn(SUBFONDOKEY, true, true, true, true, true);
		addColumn(SUBFONDOSCHEDA, true, true, true, true, true);
		addColumn(SUBFONDO2, true, true, true, true, true);
		addColumn(SUBFONDO2KEY, true, true, true, true, true);
		addColumn(SUBFONDO2SCHEDA, true, true, true, true, true);
		addColumn(_ROOTDESC_, true, true, true, true, true);
		addColumn(TIPOLOGIAMATERIALE, true, true, true, true, true);
		addColumn(DATACRONICA, true, true, true, true, true);
		addColumn(DATATOPICA, true, true, true, true, true);
		addColumn(SUPPORTO, true, true, true, true, true);
		addColumn(TECNICHE, true, true, true, true, true);
		addColumn(DIMENSIONE, true, true, true, true, true);
		addColumn(SCALA, true, true, true, true, true);
		addColumn(STATOCONSERVAZIONE, true, true, true, true, true);
		addColumn(DATIFRUIZIONE, true, true, true, true, true);
		addColumn(COMPILATORE, true, true, true, true, true);
		addColumn(DATACOMPILAZIONE, true, true, true, true, true);
		addColumn(NOTE, true, true, true, true, true);
		addColumn(TIPOLOGIAUNITAARCHIVISTICA, true, true, true, true, true);
		addColumn(ANNOINIZIALE, true, true, true, true, true);
		addColumn(ANNOFINALE, true, true, true, true, true);
		addColumn(SECOLOINIZIALE, true, true, true, true, true);
		addColumn(SECOLOFINALE, true, true, true, true, true);
		addColumn(CONSISTENZACARTE, true, true, true, true, true);
		addColumn(CONSISTENZASAST, true, true, true, true, true);
		addColumn(DOCUMENTICARTOGRAFICI, true, true, true, true, true);
		addColumn(CHILDREN, true, true, true, true, true);
		addColumn(CHILDRENDESC, true, true, true, true, true);

		addColumn(XMLSCHEDAF, true, false, false, false, false);
		addColumn(ENTESCHEDATORE, true, true, true, true, true);
		addColumn(ENTECOMPETENTE, true, true, true, true, true);
		addColumn(STATO, true, true, true, true, true);
		addColumn(REGIONE, true, true, true, true, true);
		addColumn(PROVINCIA, true, true, true, true, true);
		addColumn(COMUNE, true, true, true, true, true);
		addColumn(DENOMINAZIONE, true, true, true, true, true);
		addColumn(DENOMINAZIONEINDIRIZZO, true, true, true, true, true);
		addColumn(DENOMINAZIONERACCOLTA, true, true, true, true, true);
		addColumn(FONDOSPECIFICHE, true, true, true, true, true);
		addColumn(REFERENTESCIENTIFICO, true, true, true, true, true);
		addColumn(FUNZIONARIORESPONSABILE, true, true, true, true, true);

		addColumn(INDIRIZZO, true, true, false, true, false);
		addColumn(TELEFONO, true, true, false, true, false);
		addColumn(FAX, true, true, false, true, false);

		addColumn(ESTREMI, true, true, false, true, false);
		addColumn(STORIAARCHIVISTICA, true, true, false, true, false);
		addColumn(SOGGETTOPRODUTTOREKEY, true, true, false, true, false);
		addColumn(SOGGETTOPRODUTTORE, true, true, false, true, false);

		addColumn(TIPOSOGGETTOCONSERVATORE, true, true, false, true, false);
		addColumn(EMAIL, true, true, false, true, false);
		addColumn(SERVIZIOPUB, true, true, false, true, false);
		addColumn(ORARIOAPERTURA, true, true, false, true, false);
		addColumn(SCHEDECONSERVATORI, true, true, false, true, false);
		addColumn(SCHEDECONSERVATORIURL, true, true, false, true, false);
		addColumn(RISORSEESTERNE, true, true, false, true, false);
		addColumn(RISORSEESTERNEURL, true, true, false, true, false);

		addColumn(TIPOLOGIA, true, true, false, true, false);
		addColumn(SISTEMAADERENTE, true, true, false, true, false);
		addColumn(SCHEDAPROVENIENZAURL, true, true, false, true, false);

		addColumn(ALTREDENOMINAZIONI, true, true, false, true, false);
		addColumn(DATAESISTENZA, true, true, false, true, false);
		addColumn(DATAMORTE, true, true, false, true, false);
		addColumn(LUOGONASCITA, true, true, false, true, false);
		addColumn(LUOGOMORTE, true, true, false, true, false);
		addColumn(SEDE, true, true, false, true, false);
		addColumn(NATURAGIURIDICA, true, true, false, true, false);
		addColumn(TIPOENTE, true, true, false, true, false);
		addColumn(AMBITOTERRITORIALE, true, true, false, true, false);
		addColumn(TITOLOSP, true, true, false, true, false);
	}

}
