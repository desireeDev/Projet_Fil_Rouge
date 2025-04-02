package utils;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.poi.util.Units;
import com.example.bibliotheque.Model.Livre;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.math.BigInteger;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBookmark;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
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

        // Grand titre centré
        XWPFParagraph title = doc.createParagraph();
        title.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun titleRun = title.createRun();
        titleRun.setText("Livre de la bibliothèque");
        titleRun.setBold(true);
        titleRun.setFontSize(28);
        titleRun.addCarriageReturn();
        titleRun.addBreak();

        XWPFParagraph devParagraph = doc.createParagraph();
        devParagraph.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun devRun = devParagraph.createRun();
        devRun.setText("Développeurs : Asma, Syntiche, Isaac, Léo");
        devRun.setFontSize(14);
        devRun.setItalic(true);
        devRun.addBreak();
        devRun.addBreak();

        try {
            String imgFile = "src/main/java/utils/myimg.png";
            FileInputStream is = new FileInputStream(imgFile);
            titleRun.addPicture(is,
                    Document.PICTURE_TYPE_JPEG,
                    imgFile,
                    Units.toEMU(400),  // largeur en EMUs
                    Units.toEMU(300)); // hauteur en EMUs
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

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

            XWPFParagraph infoParagraph = doc.createParagraph();
            XWPFRun infoRun = infoParagraph.createRun();
            infoRun.setText("Auteur : " + livres[i].getAuteur());
            infoRun.addBreak();
            infoRun.setText("Parution : " + livres[i].getParution());
            infoRun.addBreak();
            infoRun.setText("Position : Rangée " + livres[i].getRangee()+ " Colonne " + livres[i].getColonne());
            infoRun.addBreak();
            infoRun.setText("Présentation : " + livres[i].getPresentation());
            paragraph.setPageBreak(true);

            String imagePath = livres[i].getPathImage();
            if (imagePath != null && !imagePath.isEmpty()) {
                try {
                    InputStream imageStream;
                    boolean isUrl = imagePath.startsWith("http://") || imagePath.startsWith("https://");

                    if (isUrl) {
                        // Lien distant
                        URL url = new URL(imagePath);
                        imageStream = url.openStream();
                    } else {
                        // Chemin local
                        File imageFile = new File(imagePath);
                        if (!imageFile.exists()) {
                            throw new IOException("Image non trouvée : " + imagePath);
                        }
                        imageStream = new FileInputStream(imageFile);
                    }

                    XWPFParagraph imageParagraph = doc.createParagraph();
                    imageParagraph.setAlignment(ParagraphAlignment.CENTER);
                    XWPFRun imageRun = imageParagraph.createRun();
                    imageRun.addPicture(imageStream,
                            Document.PICTURE_TYPE_JPEG,
                            imagePath,
                            Units.toEMU(250),
                            Units.toEMU(380));
                    imageStream.close();

                }

                catch (Exception e) {
                    System.out.println("Erreur lors de l'ajout de l'image pour le livre : " + livres[i].getTitre());
                    e.printStackTrace();

                }
            }


        }
    }


    public void createTableofUnavailableBooks (XWPFDocument doc, Livre[] livres) {
        int cols = 2;
        int rows = livres.length;

        // Titre du tableau
        XWPFParagraph tableTitle = doc.createParagraph();
        tableTitle.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun titleRun = tableTitle.createRun();
        titleRun.setText("Tableau des Livres");
        titleRun.setBold(true);
        titleRun.setFontSize(16);
        titleRun.addBreak();

        // Création du tableau avec autant de lignes que de livres
        XWPFTable table = doc.createTable(rows + 1, cols); // +1 pour la ligne d'en-tête

        // Ligne d'en-tête
        XWPFTableRow headerRow = table.getRow(0);
        headerRow.getCell(0).setText("Titre");
        headerRow.getCell(1).setText("Disponibilité");

        // Lignes des livres
        for (int i = 0; i < livres.length; i++) {
            XWPFTableRow tableRow = table.getRow(i + 1); // +1 car la ligne 0 est l'en-tête

            // Colonne 1 : Titre
            tableRow.getCell(0).setText(livres[i].getTitre());

            // Colonne 2 : Disponibilité
            String dispo = livres[i].isEmprunte() ? "Indisponible" : "Disponible";
            tableRow.getCell(1).setText(dispo);
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
