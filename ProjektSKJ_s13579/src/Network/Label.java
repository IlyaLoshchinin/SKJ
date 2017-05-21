package Network;


import java.util.Iterator;

public class Label implements Iterator {
    private String value;
    private Label[] child = new Label[2];
    private int CountChild = 0;
    public int valueNext =  0;
    private int idOfGraf ;
    public static int countOfLabel;

    public Label(String value) {
        this.value = value;
        this.idOfGraf = ++countOfLabel;
    }

    public int getIdOfGraf() {
        return idOfGraf;
    }

    public String getValue() {
        return value;
    }


    public Label getChildCr() {
        if(getCountChild() == 1){
            return child[0];
        }else if(getCountChild() == 2){
            if(valueNext == 0){
                valueNext++;
                return child[0];
            }else if(valueNext == 1) {
                valueNext++;
                return child[1];
            }
        } else{
            System.out.println("Doesn't have a child or maybe have 2 child");
            return null;
        }
        return null;
    }

    public void setValueNext0() {
        this.valueNext = 0;
    }

    public Label getChild() {
        if(getCountChild() == 1){
            return child[0];
        }else if(getCountChild() == 2){
            return child[1];
        } else{
            System.out.println("Doesn't have a child or maybe have 2 child");
            return null;
        }
    }

    public Label[] getChilds(){
        return child;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setChild(Label child) {
        if (CountChild >= 2){
            System.out.println("You can't add more child to this label!");
            return;
        }
        switch (CountChild) {
            case 0:
                this.child[0] = child;
                CountChild++;
                break;
            case 1:
                this.child[1] = child;
                CountChild++;
                break;
        }

    }

    public void deleteChild(int index){
        this.child[index] = null;
    }

    public int getCountChild() {
        return CountChild;
    }

    @Override
    public boolean hasNext() {
        if(child[0] != null || child[1] != null){
            return true;
        }
        return false;
    }

    @Override
    public Label next() {
        return getChildCr();
    }


}
