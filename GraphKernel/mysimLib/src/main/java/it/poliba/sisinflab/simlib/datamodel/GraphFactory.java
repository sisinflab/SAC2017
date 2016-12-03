package it.poliba.sisinflab.simlib.datamodel;

import it.poliba.sisinflab.simlib.datamodel.abstat.AbstatGraph;
import it.poliba.sisinflab.simlib.exceptions.InvalidGraphException;
import it.poliba.sisinflab.simlib.input.Statement;
import it.poliba.sisinflab.simlib.input.StatementFileReader;
import it.poliba.sisinflab.simlib.input.pathfile.TabPathsFileReader;
import it.poliba.sisinflab.simlib.input.triplefile.TabTriplesFileReader;
import it.poliba.sisinflab.simlib.datamodel.pathmodel.PathGraph;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Creates {@link Graph} objects based on some given parameters. It contains only static methods.
 *
 * @author Giorgio Basile
 * @since 1.0
 */
public class GraphFactory {

    public static final String TRIPLE_GRAPH = "triple_graph";
    public static final String PATH_GRAPH = "path_graph";
    public static final String ABSTAT_GRAPH = "asbtat_graph";

    /**
     * Creates a Graph object that represents the structure described in the input file, using the proper
     * data representation through the graphType value
     *
     * @param graphFile file containing the graph statements
     * @param graphType specifies the proper data model
     * @return the resulting {@link Graph} object
     * @throws IOException if the inputFile doesn't exist
     * @throws InvalidGraphException if the graphType is unknown
     */
    public static Graph create(File graphFile, String graphType) throws IOException, InvalidGraphException {

        Graph graph;
        if(graphType.equals(TRIPLE_GRAPH)){
            StatementFileReader tfr = new TabTriplesFileReader(graphFile);
            List<Statement> triples = tfr.readFile();

            //building graph from triples list
            graph = new Graph();
            graph.build(triples);

        }else if(graphType.equals(PATH_GRAPH)){
            StatementFileReader tfr = new TabPathsFileReader(graphFile);
            List<Statement> statements = tfr.readFile();

            //building graph from path list
            graph = new PathGraph();
            graph.build(statements);

        }else
            throw new InvalidGraphException("Unknown graph type");

        return graph;
    }

    /**
     * Creates a Graph object that represents the structure described in the input file, using the proper
     * data representation through the graphType value
     *
     * @param graphFile file containing the graph statements
     * @param itemsIDs list containing the items IDs
     * @param graphType specifies the proper data model
     * @return the resulting {@link Graph} object
     * @throws IOException if the inputFile doesn't exist
     * @throws InvalidGraphException if the graphType is unknown
     */
    public static Graph create(File graphFile, List<String> itemsIDs, String graphType) throws IOException, InvalidGraphException {
        Graph graph = create(graphFile, graphType);
        graph.markItems(itemsIDs);
        return graph;
    }

    /**
     * Creates an ABSTAT graph object
     *
     * @param graphURI URI of the graph to explore
     * @return a AbstatGraph object
     */
    public static AbstatGraph createAbstatGraph(String graphURI){
        AbstatGraph graph = new AbstatGraph(graphURI);
        return graph;
    }

}
