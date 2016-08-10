
import edu.stanford.nlp.dcoref.CorefChain;
import edu.stanford.nlp.dcoref.CorefCoreAnnotations;
import edu.stanford.nlp.ling.CoreAnnotations.*;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;


import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by kilby on 7/19/16.
 */
public class NLP
{
    static Properties prop = new Properties();
    static StanfordCoreNLP pipeline;

    public static void main(String[] arg)
    {
        prop.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
        String[] facey = new String[] {"I want to go skiing", "What's the best steak in seattle", "all sushi in portland",
                    "great architecture in rome", "top 5 coffee places in seattle", "Garrett wants to be a ninja", "Best photo spots", "Best sushi dishes"};
        pipeline = new StanfordCoreNLP(prop);

        System.out.println("*********************");
        for(String s : facey)
        {
            SubjectFind test = new SubjectFind(pipeline, s);

            System.out.println(s);
            System.out.println(test.annotate());
            System.out.println("*********************");

        }
    }
}
