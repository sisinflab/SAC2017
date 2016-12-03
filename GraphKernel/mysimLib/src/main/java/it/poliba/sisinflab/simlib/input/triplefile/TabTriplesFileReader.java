package it.poliba.sisinflab.simlib.input.triplefile;

import it.poliba.sisinflab.simlib.exceptions.InvalidStatementException;
import it.poliba.sisinflab.simlib.input.Statement;
import it.poliba.sisinflab.simlib.input.StatementFileReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Reads files made by Triple statements
 *
 * @author Giorgio Basile
 * @since 1.0
 */

public class TabTriplesFileReader extends StatementFileReader {

    public TabTriplesFileReader(String fileName) throws FileNotFoundException {
        super(fileName);
    }

    public TabTriplesFileReader(File file) throws FileNotFoundException {
        super(file);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Statement> readFile() throws IOException {
        System.out.println("Reading file: " + inputFile.getName());

        List<Statement> tripleList = Files.lines(Paths.get(inputFile.getPath()), Charset.forName("UTF-8"))
                .map(this::processLine)
                .flatMap(Collection::stream)
                .collect(toList());

        System.out.println("Triples: " + tripleList.size());

        return tripleList;
    }

    private List<Triple> processLine(String line){
        List<Triple> tripleList = new ArrayList<>();

        String lineFetch[] = line.replace(" ", "").split("\t");

        if(lineFetch.length != 3){
            throw new InvalidStatementException(line + " is not a valid Triple statement");
        }

        String subject = lineFetch[0];
        String predicate = lineFetch[1];
        System.out.println(subject+" "+predicate);
        //creating an array with all the objects (removing squared brackets)
        String objectsString = lineFetch[2].substring(1, lineFetch[2].length() - 1);
        String objectsFetch[] = objectsString.split(",");

        //creating triple objects
        for (String object : objectsFetch) {
            Triple t = new Triple(subject, predicate, object);
            tripleList.add(t);
        }

        if(subject == null || objectsFetch.length == 0 || predicate == null)
            throw new InvalidStatementException(line + " cannot be converted in a valid Triple object");

        return tripleList;
    }
}
