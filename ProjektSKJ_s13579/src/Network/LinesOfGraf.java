package Network;

import java.util.LinkedList;


public class LinesOfGraf extends LinkedList<String> {

    public void add(int id,String label,int from,int to) {
        super.add(id+ " [label = \""+ label +"\"];" +  from + " -> " + to);
    }
    public void add(int id,String label) {
        super.add(id+ " [label = \""+ label +"\"];");
    }

    @Override
    public Object[] toArray() {
        return super.toArray();
    }




}
