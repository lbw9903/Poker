import java.lang.reflect.Array;
import java.util.*;
import java.util.Scanner;


class Deck {
    final int CARD_NUM = 52;	// 카드의 개수
    Card cardArr[] = new Card[CARD_NUM];
    List<Card> pickCards = new ArrayList<>(5);

    Deck () {	// Deck의 카드를 초기화한다.
        int i=0;

        for(int k=Card.KIND_MAX; k > 0; k--)
            for(int n=0; n < Card.NUM_MAX ; n++)
                cardArr[i++] = new Card(k, n+1);
    }

    Card pick(int index) {// 지정된 위치(index)에 있는 카드 하나를 꺼내서 반환
        Card pickCard = cardArr[index];
        pickCards.add(pickCard);
        return cardArr[index];
    }

    Card pick() {			// Deck에서 카드 하나를 선택한다.
        int index = (int)(Math.random() * CARD_NUM);
        if ((pickCards.contains(cardArr[index]))) {
            index = (int)(Math.random() * CARD_NUM);
        }
        return pick(index);
    }

    void shuffle() { // 카드의 순서를 섞는다.
        for(int i=0; i < cardArr.length; i++) {
            int r = (int)(Math.random() * CARD_NUM);

            Card temp = cardArr[i];
            cardArr[i] = cardArr[r];
            cardArr[r] = temp;
        }
    }
}


class Card implements Comparator<Card> {
    static final int KIND_MAX = 4;	// 카드 무늬의 수
    static final int NUM_MAX  = 13;	// 무늬별 카드 수

    static final int SPADE   = 4;
    static final int DIAMOND = 3;
    static final int HEART   = 2;
    static final int CLOVER  = 1;

    int kind;
    int number;

    Card() {
        this(SPADE, 1);
    }

    Card(int kind, int number) {
        this.kind = kind;
        this.number = number;
    }

    public int compare(Card c1, Card c2) {
        if (c1.number > c2.number) {
            return c1.number - c2.number;
        } else return c2.number - c1.number;
    }

    public String toString() {
        String[] kinds = {"", "CLOVER", "HEART", "DIAMOND", "SPADE"};
        String numbers = "123456789XJQKA"; // 숫자 10은 X로 표현
        return "kind : " + kinds[this.kind]
                + ", number : " + numbers.charAt(this.number);
    }
}

class Player{
    Rank rank = new Rank();
    String nickName;
    int money = 10000;
    int point;

    Card[] cards = new Card[5]; //player의 패를 저장할 Card배열


    /*
    Player (String nickName, int point,int moneny, Card c1, Card c2, Card c3, Card c4, Card c5) {
        this.nickName = nickName;
    }
     */

    Player () {}

    Player (String nickName, int money) {
        this.nickName = nickName;
        this.money = money;
    }
}

class Dealer{
    Rank rank = new Rank();
}

class Rank{
    Card card = new Card();
    int rankPoint;
    String winner;



}



public class Poker{
    public static void  main(String[] args) {


        System.out.print("게임에 참가할 인원을 입력해주세요.\n최대 4명까지 입력가능합니다.\n");

        Scanner scan = new Scanner(System.in);


        int playerNums = Integer.parseInt(scan.nextLine());//인원 수 입력


        Player[] players = new Player[playerNums]; // player들의 정보 저장할 객체 생성

        while (true) { //인원 수가 안 맞았을 때 다시 입력
            if (playerNums > 4 || playerNums <= 0) {
                System.out.println("인원 수는 최대 4명까지 가능합니다.\n올바른 인원 수를 입력해주세요.");
                int i = Integer.parseInt(scan.nextLine());
                playerNums = i;
            } else break;
        }

        int nickNameSet=0;
        while (true) { //닉네임이 20자 넘어가면 다시 입력
            System.out.print("플레이어의 이름을 입력해주세요.\n최대 20자까지 가능합니다.\n");
            players[nickNameSet] = new Player(scan.nextLine(), 10000);
            if (players[nickNameSet].nickName.length() > 20){
                nickNameSet--;
                System.out.print("이름은 최대 20자까지만 가능합니다.\n다시 입력해주세요\n");
            }
            nickNameSet++;
            if (nickNameSet == playerNums) break;
        }

        System.out.println("게임을 시작합니다.");

        Deck deck = new Deck();
        Card card = new Card();
        Rank rank = new Rank();


        for (int i = 0; i < playerNums; i++) {// 각각의 player한테 카드나눠주기
            deck.shuffle();
            for (int j = 0; j < 5; j++) {
                card = deck.pick();
                players[i].cards[j] = card;
            }
        }

        //넘버를 오른차순으로 정렬
        Comparator<Card> arr = (c1, c2) -> Integer.compare(c1.number, c2.number);


        // 앞뒤 숫자랑 같으면 2, 다르면-1
        Comparator<Card> com = (c1, c2) -> {
            if (c1.number == c2.number) {
                return 1;
            }
            return -1;
        };


        Comparator<Player> comHigh = (p1, p2) -> { //rankPoint ==0일때(하이카드)인 경우 높은 숫자기준 오름차순 정려
            if (p1.rank.rankPoint == p2.rank.rankPoint && p1.rank.rankPoint == 0) {
                int max1 = Integer.MIN_VALUE ,max2 = Integer.MIN_VALUE;

                // 루프를 사용해 최고 숫자 찾기
                for(int i=0; i<p1.cards.length; i++) {
                    if(p1.cards[i].number > max1) {
                        max1 = p1.cards[i].number;
                    }
                }
                for(int i=0; i<p2.cards.length; i++) {
                    if(p2.cards[i].number > max2) {
                        max2 = p2.cards[i].number;
                    }
                }

                return Integer.compare(max1, max2); // 높은 숫자 가진 사람기준 오름차순
            }
            return Integer.compare(p1.rank.rankPoint, p2.rank.rankPoint); // rankPoint 기준 정렬
        };

        Comparator<Player> comOnepair = (p1, p2) -> { //rankPoint==1인 경우(Onepair)인 경우 높은 숫자 페어 기준 오름차순 정렬
            if (p1.rank.rankPoint == p2.rank.rankPoint && p1.rank.rankPoint == 1) {
                // 페어를 찾기 위해 카드에서 같은 숫자를 가진 카드를 찾아서 비교
                int pair1 = Integer.MIN_VALUE, pair2 = Integer.MIN_VALUE;

                // p1의 카드에서 페어 숫자 찾기
                for (int i = 0; i < p1.cards.length; i++) {
                    for (int j = i + 1; j < p1.cards.length; j++) {
                        if (p1.cards[i].number == p1.cards[j].number) {
                            pair1 = p1.cards[i].number;  // 첫 번째 페어 숫자
                            break;
                        }
                    }
                    if (pair1 != Integer.MIN_VALUE) break;  // 페어를 찾으면 반복문 종료
                }

                // p2의 카드에서 페어 숫자 찾기
                for (int i = 0; i < p2.cards.length; i++) {
                    for (int j = i + 1; j < p2.cards.length; j++) {
                        if (p2.cards[i].number == p2.cards[j].number) {
                            pair2 = p2.cards[i].number;  // 두 번째 페어 숫자
                            break;
                        }
                    }
                    if (pair2 != Integer.MIN_VALUE) break;  // 페어를 찾으면 반복문 종료
                }

                // 페어 숫자 기준으로 비교 (오름차순)
                return Integer.compare(pair1, pair2);
            }

            return Integer.compare(p1.rank.rankPoint, p2.rank.rankPoint); // rankPoint 기준 정렬
        };


        if (players.length > 0) { //같은 숫자 찾기
            for (int i = 0; i < playerNums; i++) {
                Arrays.sort(players[i].cards, arr);
                for (int j = 0; j < players[0].cards.length - 1; j++) { //정렬한 카드 앞뒤 비교
                    System.out.print(com.compare(players[i].cards[j], players[i].cards[j + 1]));
                    if (com.compare(players[i].cards[j], players[i].cards[j + 1]) > 0) {
                        players[i].rank.rankPoint++;
                    }
                System.out.println(players[i].cards[j]);
                }
                System.out.println(players[i].cards[4]);
                System.out.println(players[i].rank.rankPoint);
            }
        }

            Arrays.sort(players, comHigh.thenComparing(comOnepair)); // 여러 기준 동시 적용

            for (int i=0; i<playerNums; i++) {
                System.out.println(players[i].nickName+","+players[i].rank.rankPoint);
            }

    }
}

