//import java.util.*;
//class Student implements Comparable {
//    String name;
//    int ban;
//    int no;
//    int kor;
//    int eng;
//    int math;
//    int total;
//    int schoolRank;
//
//    Student(String name, int ban, int no, int kor, int eng, int math) {
//        this.name = name;
//        this.ban = ban;
//        this.no = no;
//        this.kor = kor;
//        this.eng = eng;
//        this.math = math;
//        total = kor + eng + math;
//    }
//
//    int getTotal() {
//        return total;
//    }
//
//    float getAverage() {
//        return (int) ((getTotal() / 3f) * 10 + 0.5) / 10f;
//    }
//
//    public int compareTo(Object o) {
//        if (o instanceof Student) {
//            Student s = (Student) o;
//            return this.total - s.total;
//        }
//        return -1;
//    }
//    public String toString() {
//        return name
//                +","+ban
//                +","+no
//                +","+kor
//                +","+eng
//                +","+math
//                +","+getTotal()
//                +","+getAverage()
//                +","+schoolRank;
//    }
//}//classStudent
//class Pra{
//    public static void calculateSchoolRank(List list){
//        Collections.sort(list);// list .
//        int prevRank=-1; //
//        int prevTotal=-1; //
//        int length=list.size();
//
//        for (int i=0; i<list.size(); i++) {
//            Iterator it = list.iterator();
//            if (it.hasNext()) {
//                it.next();
//
//            }
//
//        }
//
//    }
//    public static void main(String[] args){
//        ArrayList list=new ArrayList();
//        list.add(new Student("이자바",2,1,70,90,70));
//        list.add(new Student("안자바",2,2,60,100,80));
//        list.add(new Student("홍길동",1,3,100,100,100));
//        list.add(new Student("남궁성",1,1,90,70,80));
//        list.add(new Student("김자바",1,2,80,80,90));
//        calculateSchoolRank(list);
//        Iterator it=list.iterator();
//        while(it.hasNext())
//            System.out.println(it.next());
//    }
//}


import java.util.*;

import static java.util.HashSet.newHashSet;

class SutdaCard{
    int num;
    boolean isKwang;
    SutdaCard(){
        this(1,true);
    }
    SutdaCard(int num,boolean isKwang){
        this.num=num;
        this.isKwang=isKwang;
    }
    public boolean equals(Object obj){
        if(obj instanceof SutdaCard){
            SutdaCard c=(SutdaCard)obj;
            return num==c.num&&isKwang==c.isKwang;
        }else{
            return false;
        }
    }
    public String toString(){
        return num+(isKwang?"K":"");
    }


   public int hashCode() {
        return toString().hashCode();
    }

}
class Pra{
    public static void main(String[]args){
        SutdaCard c1=new SutdaCard(3,true);
        SutdaCard c2=new SutdaCard(3,true);
        SutdaCard c3=new SutdaCard(1,true);
        HashSet set=new HashSet();
        set.add(c1);
        set.add(c2);
        set.add(c3);
        System.out.println(set);

        System.out.println("123");
    }
}

