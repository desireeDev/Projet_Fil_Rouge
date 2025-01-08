package utils;

import javax.xml.XMLConstants;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.xml.sax.SAXException;
import java.io.File;
import java.io.IOException;

public class FileValidator {

    // Définition directe des fichiers dans resources
    private static final String FICHIER_XML = "src/main/resources/Biblio.xml";
    private static final String FICHIER_XSD = "src/main/resources/Biblio.xsd";

    /**
     * Méthode pour valider le fichier XML avec le fichier XSD
     */
    public static boolean validerXML() {
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new File(FICHIER_XSD));
            Validator validator = schema.newValidator();
            validator.validate(new javax.xml.transform.stream.StreamSource(new File(FICHIER_XML)));
            System.out.println("✅ Le fichier XML est valide !");
            return true;
        } catch (SAXException | IOException e) {
            System.out.println("❌ Erreur de validation : " + e.getMessage());
            return false;
        }
    }

    /**
     * Méthode principale pour exécuter la validation
     */
    public static void main(String[] args) {
        boolean estValide = validerXML();
        if (estValide) {
            System.out.println("Hola Le fichier XML est conforme !");
        } else {
            System.out.println("Sorry Le fichier XML n'est PAS conforme.");
        }
    }
}
