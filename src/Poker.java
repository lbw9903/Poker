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
    int win=0;
    int lose=0;

    Card[] cards = new Card[5]; //player의 패를 저장할 Card배열


    /*
    Player (String nickName, int point,int moneny, Card c1, Card c2, Card c3, Card c4, Card c5) {
        this.nickName = nickName;
    }
     */

    Player () {}

    Player (String nickName, int money, int win, int lose) {
        this.nickName = nickName;
        this.money = money;
        this.win = win;
        this.lose = lose;
    }
}

class Dealer{

    void giveCards(Deck deck, Player[] players) { //player들에게 카드 나눠주기
        deck.shuffle();
        for (int i = 0; i < players.length; i++) { // player수만큼 나눠주기
            for (int j = 0; j < 5; j++) {
                Card card = deck.pick();
                players[i].cards[j] = card;
            }
        }
    }

    void rankCheck(Player[] players) {
        // 카드 숫자를 오름차순 정렬
        Comparator<Card> arr = (c1, c2) -> Integer.compare(c1.number, c2.number);

        // 두 카드 비교하여 숫자가 같으면 1, 다르면 -1을 반환
        Comparator<Card> com = (c1, c2) -> {
            if (c1.number == c2.number) {
                return 1;
            }
            return -1;
        };

        //rankPoint같으면 가장
        Comparator<Player> comHigh = (p1, p2) -> {
            if (p1.rank.rankPoint == p2.rank.rankPoint) {
                int max1 = Integer.MIN_VALUE, max2 = Integer.MIN_VALUE;

                // p1의 가장 큰 카드 숫자 찾기
                for (Card card : p1.cards) {
                    if (card.number > max1) {
                        max1 = card.number;
                    }
                }

                // p2의 가장 큰 카드 숫자 찾기
                for (Card card : p2.cards) {
                    if (card.number > max2) {
                        max2 = card.number;
                    }
                }

                // 가장 큰 카드 숫자 기준 내림차순 정렬
                return Integer.compare(max2, max1);
            }
            // rankPoint가 다르면 rankPoint 기준 내림차순 정렬
            return Integer.compare(p2.rank.rankPoint, p1.rank.rankPoint);
        };

        //OnePair일 경우 정렬 기준 설정
        Comparator<Player> comOnepair = (p1, p2) -> {
            if (p1.rank.rankPoint == p2.rank.rankPoint && p1.rank.rankPoint == 1) {
                int pair1 = Integer.MIN_VALUE, pair2 = Integer.MIN_VALUE;

                // p1의 페어 숫자 찾기
                for (int i = 0; i < p1.cards.length; i++) {
                    for (int j = i + 1; j < p1.cards.length; j++) {
                        if (p1.cards[i].number == p1.cards[j].number) {
                            pair1 = p1.cards[i].number;  // p1의 페어 숫자
                            break;
                        }
                    }
                    if (pair1 != Integer.MIN_VALUE) break; // 페어를 찾으면 종료
                }

                // p2의 페어 숫자 찾기
                for (int i = 0; i < p2.cards.length; i++) {
                    for (int j = i + 1; j < p2.cards.length; j++) {
                        if (p2.cards[i].number == p2.cards[j].number) {
                            pair2 = p2.cards[i].number;  // p2의 페어 숫자
                            break;
                        }
                    }
                    if (pair2 != Integer.MIN_VALUE) break; // 페어 찾으면 종료
                }

                // 페어 숫자 기준으로 내림차순 정렬
                return Integer.compare(pair2, pair1);
            }
            // rankPoint가 다르면 rankPoint 기준으로 내림차순 정렬
            return Integer.compare(p2.rank.rankPoint, p1.rank.rankPoint);
        };

        if (players.length > 0) {
            for (int i = 0; i < players.length; i++) {
                Arrays.sort(players[i].cards, arr); // 카드 오름차순 정렬

                // 카드 숫자 비교하여 앞뒤 카드 비교
                for (int j = 0; j < players[i].cards.length - 1; j++) {
                    if (com.compare(players[i].cards[j], players[i].cards[j + 1]) > 0) {
                        players[i].rank.rankPoint++; // 숫자 순서가 맞지 않으면 rankPoint 증가
                    }
                }
            }
        }

        // 각 플레이어의 rankPoint를 설정하기 위해 페어 수를 확인
        for (int i = 0; i < players.length; i++) {
            Arrays.sort(players[i].cards, arr); // 카드 다시 오름차순으로 정렬

            int pairCount = 0; // 페어의 개수 카운트
            for (int j = 0; j < players[i].cards.length - 1; j++) {
                if (players[i].cards[j].number == players[i].cards[j + 1].number) {
                    pairCount++;
                    j++;
                }
            }

            // 페어 개수에 따라 rankPoint 설정
            if (pairCount == 1) {
                players[i].rank.rankPoint = 1;  // 원페어
            } else if (pairCount == 2) {
                players[i].rank.rankPoint = 2;  // 투페어
            } else {
                players[i].rank.rankPoint = 0;  // 하이 카드
            }
        }

        // rankPoint 기준으로 내림차순 정렬, rankPoint가 같으면 가장 큰 카드 숫자 기준 내림차순 정렬
        Arrays.sort(players, comHigh.thenComparing(comOnepair));

        Player winner = players[0]; //가장 높은 rankPoint가 우승자

        winner.money += 100; //돈과, 승 추가
        winner.win += 1;

        for (int i=1; i < players.length; i++) { //패배한 사람들한테 패배 수 증가
            players[i].lose += 1;
        }

        for (Player player : players) {
            System.out.println(player.nickName + "의 rank: " + player.rank.rankName() +", 돈:"+ player.money+", 승리:" + player.win + ", 패배" + player.lose);
        }


    }

}


class Rank{
    int rankPoint;


    public String rankName() {
        if (rankPoint == 0 ) {
            return "하이 카드";
        } else if (rankPoint == 1) {
            return "원페어";
        } else return "rank";
    }

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
            players[nickNameSet] = new Player(scan.nextLine(), 10000,0,0);
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
        Dealer dealer = new Dealer();

        for (int i=0; i<100; i++) { //게임횟수

            System.out.println(i+1+"번째 게임결과");
            // Dealer의 카드나눠주는 메서드
            dealer.giveCards(deck, players);

            dealer.rankCheck(players);

        }



//        for (int i=0; i<playerNums; i++) {
//            System.out.println(players[i].nickName+",rank: "+rank.rankName());
//        }

    }
}

