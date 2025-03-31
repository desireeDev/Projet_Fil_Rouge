package utils;

import com.example.bibliotheque.Model.Livre;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BookExporter {
    public BookExporter() {
    }
    /**
     * Convertion
     * @param books
     * @param filePath
     */
    public void booksToWord(TableView<Livre> books, String filePath, String fileName) {
        // TableView est un composant d'interface utilisateur graphique lié à JavaFX,
        // pour etre utilisé dans des boucles ou autre faut toujours le convertir en
        // ObservableList<T> (aussi lié à JavaFX) avec la méthode getItems(), c'est chiant
        // donc on le convertie en un truc plus pratique, càd en tableau
        ObservableList<Livre> livreObservableList = books.getItems();
        Livre[] livresArray = new Livre[livreObservableList.size()];

        for(int i=0; i<livresArray.length; i++) {
            livresArray[i] = livreObservableList.get(i);
        }
        booksToWord(livresArray, filePath, fileName);
        //Si on veut faire avec des listes, juste cette ligne suffit
        //BooksToWord(books.getItems().stream().toList(), filePath);
    }

    /**
     * Convertie des livres
     * @param books
     * @param fileName
     */
    public void booksToWord(Livre[] books, String filePath, String fileName) {
        XWPFDocument wordDocument = new XWPFDocument();

        //entête
        createHeader(wordDocument, fileName);
        //page de garde
        createCoverPage(wordDocument);
        //Sommaire
        createTOC(wordDocument, books);
        //Chapitres
        createChapters(wordDocument, books);
        //tableau des livres non disponibles
        createTableofUnavailableBooks(wordDocument, books);

        try (FileOutputStream out = new FileOutputStream(filePath)) {
            wordDocument.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createCoverPage(XWPFDocument doc) {

    }
    /**
     * Création d'une entête presente dans toutes les pages du fichier word
     * @param doc
     * @param docName
     */
    public void createHeader(XWPFDocument doc, String docName) {
        // XWPFHeaderFooterPolicy sert juste à gérer la police des entetes
        // mais est obligatoire pour le constructeur de ce fils de pute de XWPFHeader
        XWPFHeaderFooterPolicy headerFooterPolicy = new XWPFHeaderFooterPolicy(doc);
        // Ajouter une en-tête
        XWPFHeader header = headerFooterPolicy.createHeader(XWPFHeaderFooterPolicy.DEFAULT);
        XWPFParagraph headerParagraph = header.createParagraph();
        // XWPFRun représente une séquence de texte à l'intérieur d'un paragraphe
        XWPFRun headerRun = headerParagraph.createRun();

        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = myDateObj.format(myFormatObj);

        headerRun.setText(formattedDate + "\r\n" + docName);
    }

    /**
     * Création d'un Table Of Chapter (TOC), un sommaire quoi
     * @param doc
     * @param livres
     */
    public void createTOC(XWPFDocument doc, Livre[] livres) {

        //Création de la table des matières (TOC)
        XWPFParagraph tocParagraph = doc.createParagraph();
        XWPFRun tocRun = tocParagraph.createRun();
        tocParagraph.setAlignment(ParagraphAlignment.CENTER);
        tocRun.setText("Table des Matières");
        tocRun.setBold(true);
        tocRun.setFontSize(16);
        tocRun.addCarriageReturn();
        //tocRun.addBreak();
        XWPFParagraph paragraph = doc.createParagraph();

        for(int i=0; i< livres.length; i++) {
            //Les classes CTMachin (CT pour Complex Type) sont des XMLBeans
            // qui représentent des structures de données XML généré et presentent automatiquement
            // dans les doc office en général.
            // Ils définissent des éléments, des attributs, fin bref des trucs à l'interieur du doc
            //CTHyperlink represente du coup un lien hypertexte
            CTHyperlink hyperlink = paragraph.getCTP().addNewHyperlink();
            // Avec cette méthode associe le lien hypertexte à un signet avec le même texte d'ancrage
            // Bon, histoire que ça soit diff du text affiché (et ausso au cas où i lait des livres avec mm titre),
            // je rajoute le num de placement dans le tableau juste devant
            hyperlink.setAnchor(i + livres[i].getTitre());
            // addNewR : "add New Run", addNewT : "add New Text", setStringValue : texte affiché
            hyperlink.addNewR().addNewT().setStringValue(i + ". "+ livres[i].getTitre());
            //XWPFRun représente une séquence de texte à l'intérieur d'un paragraphe
            XWPFRun lineBreakRun = paragraph.createRun();
            lineBreakRun.addCarriageReturn();
        }
        tocParagraph.setPageBreak(true);
    }

    /**
     * Création des chapitres, un chapitre équivaut à un livre
     * @param doc
     * @param livres
     */
    public void createChapters(XWPFDocument doc, Livre[] livres) {
        for(int i = 0; i < livres.length; i++) {
            XWPFParagraph paragraph = doc.createParagraph();

            //Titre du livre
            XWPFRun run = paragraph.createRun();
            run.setText(livres[i].getTitre());
            run.setStyle("Heading1");
            run.setBold(true);
            run.setFontSize(14);
            run.addCarriageReturn();

            // Ajouter un signet pour permettre le lien hypertexte
            //Complex Type Paragraph, on chope le XML qui represente le paragraphe en gros
            CTP ctp1 = paragraph.getCTP();
            CTBookmark bookmark = ctp1.addNewBookmarkStart();
            bookmark.setName(i + livres[i].getTitre());
            //Les ID on les utilise pas nous, mais s'ils ne sont pas la, les signets fonctionnent pas
            bookmark.setId(BigInteger.valueOf(i+1));
            ctp1.addNewBookmarkEnd().setId(BigInteger.valueOf(i+1));

            // Ajouter du texte dans le chapitre
            XWPFRun contentParagraph = paragraph.createRun();
            String contenu  = "contenu";
            contentParagraph.setText(contenu);
            paragraph.setPageBreak(true);
        }
    }
    public void createTableofUnavailableBooks (XWPFDocument doc, Livre[] livres) {
        int cols = 2;
        int livresNondispos = 0;

        //On compte le nombres de livres non dispo et on créé un tableau avec autant de lignes
        for (int row=0; row< livres.length; row++) {
            if(livres[row].isEmprunte()) {
                livresNondispos++;
            }
        }
        XWPFTable table = doc.createTable(livresNondispos, cols);
        //setColumnWidth(table, 0, 200);

        for (int row=0,j=0; row< livres.length; row++) {
            if(livres[row].isEmprunte()) {
                XWPFTableRow tableRow = table.getRow(j);
                tableRow.getCell(0).setText(livres[row].getTitre());
                j++;
            }
        }
    }
    public  void setColumnWidth(XWPFTable table, int colIndex, int width) {
        CTTblWidth tblWidth = CTTblWidth.Factory.newInstance();
        //width : largeur
        tblWidth.setW(width);
        tblWidth.setType(STTblWidth.DXA);
        table.getCTTbl().getTblGrid().getGridColList().get(colIndex).setW(tblWidth);
    }
}
