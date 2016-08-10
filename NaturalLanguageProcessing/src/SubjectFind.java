/**
 * Created by kilby on 7/20/16.
 */
import edu.stanford.nlp.ling.CoreAnnotations.*;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation;

import edu.stanford.nlp.trees.*;
import edu.stanford.nlp.util.CoreMap;

import java.util.Collection;
import java.util.List;


public class SubjectFind
{

    private StanfordCoreNLP pipeline;
    private String query;

    protected SubjectFind(StanfordCoreNLP pipeline, String query)
    {
        this.pipeline = pipeline;
        this.query = query;
    }

    public String annotate()
    {
        Annotation document = new Annotation(query);
        pipeline.annotate(document);
        int nounCount = 0;
        String[] nouns = new String[10];
        SemanticGraph dependencies = new SemanticGraph();
        String depends = "";
        String root = null;
        String compound = null;
        List<CoreMap> sentences = document.get(SentencesAnnotation.class);


        for (CoreMap sentence : sentences)
        {

            Tree t = sentence.get(TreeCoreAnnotations.TreeAnnotation.class);
            //t.pennPrint();



            for (CoreLabel token : sentence.get(TokensAnnotation.class))
            {
                String word = token.get(TextAnnotation.class);
                String pos = token.get(PartOfSpeechAnnotation.class);

                if((pos.equalsIgnoreCase("NN") || pos.matches("NN[a-z]")) && !cityMatch(word))
                {
                    nouns[nounCount] = word;
                    nounCount++;
                }
            }


            dependencies = sentence.get(SemanticGraphCoreAnnotations.BasicDependenciesAnnotation.class);
            SemanticGraph ccProcessed = sentence.get(SemanticGraphCoreAnnotations.BasicDependenciesAnnotation.class);

            Collection<TypedDependency> deps = ccProcessed.typedDependencies();

            for (TypedDependency typedDep : deps) {
                //System.out.println("Find Root and Compound");
                //System.out.println(typedDep.toString());
                if(typedDep.gov().toString().equals("ROOT"))
                    root = typedDep.dep().toString();

                if(typedDep.reln().toString().equals("compound"))
                    compound = typedDep.dep().toString();

                GrammaticalRelation reln = typedDep.reln();
                reln.toPrettyString();


                //System.out.println("\n");
            }

        }


        if(nounCount == 1)
        {

            return nouns[0];
        }
        else
        {
            return findDOBJ(dependencies, root, compound);
        }

    }




    private boolean cityMatch(String city)
    {
        String[] cities = new String[]{"NY", "NYC", "Seattle", "Houston", "Atlanta", "LA", "Los Angeles", "San Francisco", "SF", "San Fran",
        "San Diego", "SD"};


        for(String locations : cities)
        {
            if(locations.equalsIgnoreCase(city)){return true;}
        }

        return false;
    }

    private String findDOBJ(SemanticGraph dependencies, String root, String compound)
    {
        String nmod;


       nmod = dependencies.toString();
        //System.out.println("Find DOBJ");
        //System.out.println(nmod);

       String[] strings = nmod.split("\\n");

        for(String x : strings)
        {
            if(x.contains("nmod"))
                nmod = x;

        }


        nmod = nmod.substring(nmod.indexOf("->")+3,nmod.indexOf("/"));


        //System.out.println("Print everything");
        //System.out.println(nmod + " " + root + " " + compound);

        if(compound != null)
            return compound;


        if(nmod.equals(root))
            return nmod;
        else
            return root;

    }


}
