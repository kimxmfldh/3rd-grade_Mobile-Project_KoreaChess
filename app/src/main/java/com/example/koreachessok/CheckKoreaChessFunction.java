package com.example.koreachessok;

import android.widget.Button;

public class CheckKoreaChessFunction {
    boolean flagKoreaChess = false;
    public boolean checkFunction(String beforeKoreaChess, int before_x, int before_y, String afterKoreaChess
            , int after_x, int after_y, Button[][] koreaChessButton) {
        flagKoreaChess = false;
        if (beforeKoreaChess.contains("Joll")){
            flagKoreaChess = jollFunction(before_x,before_y,after_x,after_y);
        } else if (beforeKoreaChess.contains("Pho")) {
            flagKoreaChess = phoFunction(before_x,before_y, afterKoreaChess,after_x,after_y,koreaChessButton);
        } else if (beforeKoreaChess.contains("Cha")) {
            flagKoreaChess = ChaFunction(before_x,before_y,after_x,after_y,koreaChessButton);
        } else if (beforeKoreaChess.contains("Sang")) {
            flagKoreaChess = SangFunction(before_x,before_y,after_x,after_y,koreaChessButton);
        } else if (beforeKoreaChess.contains("Ma")) {
            flagKoreaChess = maFunction(before_x,before_y,after_x,after_y,koreaChessButton);
        } else if (beforeKoreaChess.contains("Sa") || beforeKoreaChess.contains("King")) {
            flagKoreaChess = kingAndSaFunction(before_x,before_y,after_x,after_y);
        }
        return flagKoreaChess;
    }// 메소드 골라주는 역할

    private boolean kingAndSaFunction(int x, int y, int a, int b) {

        if( (3 <= a) && (a <= 5) ) {
            if( (0 <= b) && (b <= 2) ) {
                if(x == 4 && y == 1) {// 중앙 부분 --> 대각선 이동 가능
                    flagKoreaChess = true;
                } else if(x == 3 && y == 0) {//  꼭지점 -->  대각선 이동 가능
                    if( (a <= 4) && (b <= 1) ) {
                        flagKoreaChess = true;
                    }
                } else if(x == 5 && y == 0) {// 꼭지점 -->  대각선 이동 가능
                    if( (4 <= a) && (b <= 1) ) {
                        flagKoreaChess = true;
                    }
                } else if(x == 3 && y == 2) {// 꼭지점 -->  대각선 이동 가능
                    if( (a <= 4) && (1 <= b) ) {
                        flagKoreaChess = true;
                    }
                } else if(x == 5 && y == 2) {// 꼭지점 -->  대각선 이동 가능
                    if( (4 <= a) && (1 <= b) ) {
                        flagKoreaChess = true;
                    }
                } else if(x == 4 && y == 2) { // 모서리 부분 --> 대각선 이동 불가능.
                    if(b == 2) {
                        if(a == 3 || a == 5) {
                            flagKoreaChess = true;
                        } else {
                            flagKoreaChess = false;
                        }
                    } else if(b == 1 && a == 4) {
                        flagKoreaChess = true;
                    } else {
                        flagKoreaChess = false;
                    }
                } else if(x == 4 && y == 0) { // 모서리 부분 --> 대각선 이동 불가능.
                    if(b == 0) {
                        if(a == 3 || a == 5) {
                            flagKoreaChess = true;
                        } else {
                            flagKoreaChess = false;
                        }
                    } else if(b == 1 && a == 4) {
                        flagKoreaChess = true;
                    } else {
                        flagKoreaChess = false;
                    }
                } else if(x == 3 && y == 1) { // 모서리 부분 --> 대각선 이동 불가능.
                    if(b == 1 && a == 4) {
                        flagKoreaChess = true;
                    } else if(a == 3) {
                        if(b == 2 || b == 0) {
                            flagKoreaChess = true;
                        } else {
                            flagKoreaChess = false;
                        }
                    } else {
                        flagKoreaChess = false;
                    }
                } else if(x == 5 && y == 1) { // 모서리 부분 --> 대각선 이동 불가능.
                    if(b == 1 && a == 4) {
                        flagKoreaChess = true;
                    } else if(a == 5) {
                        if(b == 2 || b == 0) {
                            flagKoreaChess = true;
                        } else {
                            flagKoreaChess = false;
                        }
                    } else {
                        flagKoreaChess = false;
                    }
                } else {
                    flagKoreaChess = false;
                }
            }
        } else {
            flagKoreaChess = false;
        }
        return flagKoreaChess;
    }// 킹/사 메소드

    private boolean maFunction(int x, int y, int a, int b, Button[][] koreaChessButton) {
        if( ((a == x + 2) || (a == x - 2)) || ((b == y + 2) || (b == y - 2)) ) {
            if( ((a == x + 1) || (a == x - 1)) || ((b == y + 1) || (b == y - 1)) ) {

                if( (a < x) && (b > y) ) {
                    if(koreaChessButton[b - 1][a + 1].getText().toString().equals("empty")) {
                        flagKoreaChess = true;
                    } else {
                        flagKoreaChess = false;
                    }
                } else if( (a > x) && (b > y) ) {
                    if(koreaChessButton[b - 1][a - 1].getText().toString().equals("empty")) {
                        flagKoreaChess = true;
                    } else {
                        flagKoreaChess = false;
                    }
                } else if( (a < x) && (b < y) ) {
                    if(koreaChessButton[b + 1][a + 1].getText().toString().equals("empty")) {
                        flagKoreaChess = true;
                    } else {
                        flagKoreaChess = false;
                    }
                } else if( (a > x) && (b < y) ) {
                    if(koreaChessButton[b + 1][a - 1].getText().toString().equals("empty")) {
                        flagKoreaChess = true;
                    } else {
                        flagKoreaChess = false;
                    }
                } else {
                    flagKoreaChess = false;
                }
            } else {
                flagKoreaChess = false;
            }
        } else {
            flagKoreaChess = false;
        }
        return flagKoreaChess;    }// 마 메소드

    private boolean SangFunction(int x, int y, int a, int b, Button[][] koreaChessButton) {
        if( ((a == x + 2) || (a == x - 2) || (a == x + 3) || (a == x - 3)) ) {
            if( ((b == y + 2) || (b == y - 2) || (b == y + 3) || (b == y - 3))) {

                if( (a < x) && (b > y) ) {
                    if(koreaChessButton[b - 1][a + 1].getText().toString().equals("empty")) {
                        if(koreaChessButton[b - 2][a + 2].getText().toString().equals("empty")) {
                            flagKoreaChess = true;
                        } else {
                            flagKoreaChess = false;
                        }
                    } else {
                        flagKoreaChess = false;
                    }
                } else if( (a > x) && (b > y) ) {
                    if(koreaChessButton[b - 1][a - 1].getText().toString().equals("empty")) {
                        if(koreaChessButton[b - 2][a - 2].getText().toString().equals("empty")) {
                            flagKoreaChess = true;
                        } else {
                            flagKoreaChess = false;
                        }
                    } else {
                        flagKoreaChess = false;
                    }
                } else if( (a < x) && (b < y) ) {
                    if(koreaChessButton[b + 1][a + 1].getText().toString().equals("empty")) {
                        if(koreaChessButton[b + 2][a + 2].getText().toString().equals("empty")) {
                            flagKoreaChess = true;
                        } else {
                            flagKoreaChess = false;
                        }
                    } else {
                        flagKoreaChess = false;
                    }
                } else if( (a > x) && (b < y) ) {
                    if(koreaChessButton[b + 1][a - 1].getText().toString().equals("empty")) {
                        if(koreaChessButton[b + 2][a - 2].getText().toString().equals("empty")) {
                            flagKoreaChess = true;
                        } else {
                            flagKoreaChess = false;
                        }
                    } else {
                        flagKoreaChess = false;
                    }
                } else {
                    flagKoreaChess = false;
                }
            } else {
                flagKoreaChess = false;
            }
        } else {
            flagKoreaChess = false;
        }

        return flagKoreaChess;
    }//상 메소드

    private boolean ChaFunction(int x, int y, int a, int b, Button[][] koreaChessButton) {
        int before_x = x;
        int before_y = y;
        int after_x = a;
        int after_y = b;

        if(before_x == after_x) {
            int count = 0;
            if(before_y < after_y) {
                for(int i = before_y + 1; i < after_y; i++) {
                    if(koreaChessButton[i][before_x].getText().toString().equals("empty")) {// 비어있을 때
                    } else {// 아군이든 적군이든  +1
                        count++;
                    }
                }
                if(count == 0) {
                    flagKoreaChess = true;
                } else {
                    flagKoreaChess = false;
                }
            } else if(before_y > after_y) {
                for(int i = after_y + 1; i < before_y; i++) {
                    if(koreaChessButton[i][before_x].getText().toString().equals("empty")) {// 비어있을 때
                    }  else {// 아군이든 적군이든  +1
                        count++;
                    }
                }
                if(count == 0) {
                    flagKoreaChess = true;
                } else {
                    flagKoreaChess = false;
                }
            }
        } else if(before_y == after_y) {
            int count = 0;
            if(before_x < after_x) {
                for(int i = before_x + 1; i < after_x; i++) {
                    if(koreaChessButton[before_y][i].getText().toString().equals("empty")) {// 비어있을 때
                    } else {// 아군이든 적군이든  +1
                        count++;
                    }
                }
                if(count == 0) {
                    flagKoreaChess = true;
                } else {
                    flagKoreaChess = false;
                }
            } else if(before_x > after_x) {
                for(int i = after_x + 1; i < before_x; i++) {
                    if(koreaChessButton[before_y][i].getText().toString().equals("empty")) {// 비어있을 때
                    } else {// 아군이든 적군이든  +1
                        count++;
                    }
                }
                if(count == 0) {
                    flagKoreaChess = true;
                } else {
                    flagKoreaChess = false;
                }
            }
        } else {
            if(before_x >= 3 && before_x <= 5) {
                if(before_x == 4 && before_y == 1) {
                    if( (after_x <= 5 && after_x >= 3) && (after_y>=0 && after_y<=2) ){
                        flagKoreaChess = true;
                    }
                } else if(before_x == 4 && before_y == 8) {
                    if( (after_x <= 5 && after_x >= 3) && (after_y>=7 && after_y<=9) ){
                        flagKoreaChess = true;
                    }
                } else if(0 == before_y || before_y == 2) {
                    if( (after_x <= 5 && after_x >= 3) && (after_y>=0 && after_y<=2) ) {
                        if(after_x == 4 && after_y == 1) {
                            flagKoreaChess = true;
                        } else {
                            if(koreaChessButton[1][4].getText().toString().equals("empty")) {
                                flagKoreaChess = true;
                            } else {
                                flagKoreaChess = false;
                            }
                        }
                    } else {
                        flagKoreaChess = false;
                    }
                } else if ((9 == before_y) || (7 == before_y)){
                    if( (after_x <= 5 && after_x >= 3) && (after_y>=7 && after_y<=9) ){
                        if(after_x == 4 && after_y == 8) {
                            flagKoreaChess = true;
                        } else {
                            if(koreaChessButton[8][4].getText().toString().equals("empty")) {
                                flagKoreaChess = true;
                            } else {
                                flagKoreaChess = false;
                            }
                        }
                    } else {
                        flagKoreaChess = false;
                    }
                } else {
                    flagKoreaChess = false;
                }
            }
        }
        return flagKoreaChess;
    }// 차 메소드

    private boolean phoFunction(int x, int y, String afterKoreaChess, int a, int b, Button[][] koreaChessButton) {
        if(afterKoreaChess.contains("Pho")) {
            flagKoreaChess = false;
        } else {
            if(x == a) {// 포가 Y축 이동할 때
                int count = 0;
                if(y < b) {
                    for(int i = y + 1; i < b; i++) {
                        if(koreaChessButton[i][x].getText().toString().equals("empty")) {// 비어있을 때
                        } else {// 아군이든 적군이든 포는 +2 나머지는 +1
                            if(koreaChessButton[i][x].getText().toString().contains("Pho")) {
                                count+=2;
                            } else {
                                count++;
                            }
                        }
                    }
                    if(count == 1) {
                        flagKoreaChess = true;
                    } else {
                        flagKoreaChess = false;
                    }
                } else if(y > b) {
                    for(int i = b + 1; i < y; i++) {
                        if(koreaChessButton[i][x].getText().toString().equals("empty")) {// 비어있을 때
                        } else {// 아군이든 적군이든 포는 +2 나머지는 +1
                            if(koreaChessButton[i][x].getText().toString().contains("Pho")) {
                                count+=2;
                            } else {
                                count++;
                            }
                        }
                    }
                    if(count == 1) {
                        flagKoreaChess = true;
                    } else {
                        flagKoreaChess = false;
                    }
                }
            } else if(y == b) {// 포가 X축 이동 할 때
                int count = 0;
                if(x < a) {
                    for(int i = x + 1; i < a; i++) {
                        if(koreaChessButton[y][i].getText().toString().equals("empty")) {// 비어있을 때
                        } else {// 아군이든 상대이든 포 와 원하는 위치에 하나만 있으면 넘을 수 있음.
                            if(koreaChessButton[y][i].getText().toString().contains("Pho")) {// Pho는 카운터를 두 개를 준다.
                                count+=2;
                            } else {
                                count++;
                            }
                        }
                    }
                    if(count == 1) {
                        flagKoreaChess = true;
                    } else {
                        flagKoreaChess = false;
                    }
                } else if(x > a) {
                    for(int i = a + 1; i < x; i++) {
                        if(koreaChessButton[y][i].getText().toString().equals("empty")) {// 비어있을 때
                        } else {// 아군이든 상대이든 포 와 원하는 위치에 하나만 있으면 넘을 수 있음.
                            if(koreaChessButton[y][i].getText().toString().contains("Pho")) {// Pho는 카운터를 두 개를 준다.
                                count+=2;
                            } else {
                                count++;
                            }
                        }
                    }
                    if(count == 1) {
                        flagKoreaChess = true;
                    } else {
                        flagKoreaChess = false;
                    }
                }
            } else {
                if((3 == x) || (x == 5)) {
                    if( (y == 0) || (y == 2) ) {
                        if (koreaChessButton[1][4].getText().toString().equals("empty")) {
                            flagKoreaChess = false;
                        } else {
                            if((a == 3 || a == 5) && (b == 0 || b == 2)){
                                flagKoreaChess = true;
                            }
                        }
                    } else if((y == 9) || (y == 7)) {
                        if (koreaChessButton[8][4].getText().toString().equals("empty")) {
                            flagKoreaChess = false;
                        } else {
                            if((a == 3 || a == 5) && (b == 9 || b == 7)){
                                flagKoreaChess = true;
                            }
                        }
                    }
                } else {
                    flagKoreaChess = false;
                }
            }
        }
        return flagKoreaChess;
    }//포 메소드

    private boolean jollFunction(int x, int y, int a, int b) {
        if( (a - x == 0) && (1 == b - y) ) {
            flagKoreaChess = true;
        } else if( (y - b == 0) &&( (-1 == a - x) || (1 == a - x)) ) {
            flagKoreaChess = true;
        } else {
            if((y == 7 && x == 3) || (y == 7 && x == 5)) {
                if ((x == 3 && b == 4) || (x==5 && b == 4)) {
                    flagKoreaChess = true;
                } else {
                    flagKoreaChess = false;
                }
            } else if(y == 8 && x == 4){
                if (b >= y) {
                    flagKoreaChess = true;
                } else {
                    flagKoreaChess = false;
                }
            } else {
                flagKoreaChess = false;
            }
        }
        return flagKoreaChess;
    } //졸 메소드

    void startButtonImageHan(String selectTeam, final Button[][] koreaChessButton, int b, int a){
        if(selectTeam.equals("han")) {
            if(a == 0 && b == 0) {
                koreaChessButton[b][a].setText("hanCha");
                koreaChessButton[b][a].setBackgroundResource(R.drawable.han_cha);
            }else if(a == 1 && b == 0) {
                koreaChessButton[b][a].setText("hanSang");
                koreaChessButton[b][a].setBackgroundResource(R.drawable.han_sang);
            }else if(a == 2 && b == 0) {
                koreaChessButton[b][a].setText("hanMa");
                koreaChessButton[b][a].setBackgroundResource(R.drawable.han_ma);
            }else if(a == 3 && b == 0) {
                koreaChessButton[b][a].setText("hanSa");
                koreaChessButton[b][a].setBackgroundResource(R.drawable.han_sa);
            }else if(a == 5 && b == 0) {
                koreaChessButton[b][a].setText("hanSa");
                koreaChessButton[b][a].setBackgroundResource(R.drawable.han_sa);
            }else if(a == 6 && b == 0) {
                koreaChessButton[b][a].setText("hanMa");
                koreaChessButton[b][a].setBackgroundResource(R.drawable.han_ma);
            }else if(a == 7 && b == 0) {
                koreaChessButton[b][a].setText("hanSang");
                koreaChessButton[b][a].setBackgroundResource(R.drawable.han_sang);
            }else if(a == 8 && b == 0) {
                koreaChessButton[b][a].setText("hanCha");
                koreaChessButton[b][a].setBackgroundResource(R.drawable.han_cha);
            }else if(a == 1 && b == 2) {
                koreaChessButton[b][a].setText("hanPho");
                koreaChessButton[b][a].setBackgroundResource(R.drawable.han_pho);
            }else if(a == 7 && b == 2) {
                koreaChessButton[b][a].setText("hanPho");
                koreaChessButton[b][a].setBackgroundResource(R.drawable.han_pho);
            }else if(a == 4 && b == 1) {
                koreaChessButton[b][a].setText("hanKing");
                koreaChessButton[b][a].setBackgroundResource(R.drawable.han_king);
            }else if(a == 0 && b == 3) {
                koreaChessButton[b][a].setText("hanJoll");
                koreaChessButton[b][a].setBackgroundResource(R.drawable.han_joll);
            }else if(a == 2 && b == 3) {
                koreaChessButton[b][a].setText("hanJoll");
                koreaChessButton[b][a].setBackgroundResource(R.drawable.han_joll);
            }else if(a == 4 && b == 3) {
                koreaChessButton[b][a].setText("hanJoll");
                koreaChessButton[b][a].setBackgroundResource(R.drawable.han_joll);
            }else if(a == 6 && b == 3) {
                koreaChessButton[b][a].setText("hanJoll");
                koreaChessButton[b][a].setBackgroundResource(R.drawable.han_joll);
            }else if(a == 8 && b == 3) {
                koreaChessButton[b][a].setText("hanJoll");
                koreaChessButton[b][a].setBackgroundResource(R.drawable.han_joll);
            }else if(a == 0 && b == 9) {
                koreaChessButton[b][a].setText("choCha");
                koreaChessButton[b][a].setBackgroundResource(R.drawable.cho_cha);
            }else if(a == 1 && b == 9) {
                koreaChessButton[b][a].setText("choSang");
                koreaChessButton[b][a].setBackgroundResource(R.drawable.cho_sang);
            }else if(a == 2 && b == 9) {
                koreaChessButton[b][a].setText("choMa");
                koreaChessButton[b][a].setBackgroundResource(R.drawable.cho_ma);
            }else if(a == 3 && b == 9) {
                koreaChessButton[b][a].setText("choSa");
                koreaChessButton[b][a].setBackgroundResource(R.drawable.cho_sa);
            }else if(a == 5 && b == 9) {
                koreaChessButton[b][a].setText("choSa");
                koreaChessButton[b][a].setBackgroundResource(R.drawable.cho_sa);
            }else if(a == 6 && b == 9) {
                koreaChessButton[b][a].setText("choMa");
                koreaChessButton[b][a].setBackgroundResource(R.drawable.cho_ma);
            }else if(a == 7 && b == 9) {
                koreaChessButton[b][a].setText("choSang");
                koreaChessButton[b][a].setBackgroundResource(R.drawable.cho_sang);
            }else if(a == 8 && b == 9) {
                koreaChessButton[b][a].setText("choCha");
                koreaChessButton[b][a].setBackgroundResource(R.drawable.cho_cha);
            }else if(a == 1 && b == 7) {
                koreaChessButton[b][a].setText("choPho");
                koreaChessButton[b][a].setBackgroundResource(R.drawable.cho_pho);
            }else if(a == 7 && b == 7) {
                koreaChessButton[b][a].setText("choPho");
                koreaChessButton[b][a].setBackgroundResource(R.drawable.cho_pho);
            }else if(a == 4 && b == 8) {
                koreaChessButton[b][a].setText("choKing");
                koreaChessButton[b][a].setBackgroundResource(R.drawable.cho_king);
            }else if(a == 0 && b == 6) {
                koreaChessButton[b][a].setText("choJoll");
                koreaChessButton[b][a].setBackgroundResource(R.drawable.cho_joll);
            }else if(a == 2 && b == 6) {
                koreaChessButton[b][a].setText("choJoll");
                koreaChessButton[b][a].setBackgroundResource(R.drawable.cho_joll);
            }else if(a == 4 && b == 6) {
                koreaChessButton[b][a].setText("choJoll");
                koreaChessButton[b][a].setBackgroundResource(R.drawable.cho_joll);
            }else if(a == 6 && b == 6) {
                koreaChessButton[b][a].setText("choJoll");
                koreaChessButton[b][a].setBackgroundResource(R.drawable.cho_joll);
            }else if(a == 8 && b == 6) {
                koreaChessButton[b][a].setText("choJoll");
                koreaChessButton[b][a].setBackgroundResource(R.drawable.cho_joll);
            } else {
                koreaChessButton[b][a].setText("empty");
                koreaChessButton[b][a].setBackgroundResource(R.drawable.button_default_layout);
            }
        }
    }// 버튼 시작할때 초기화. 내가 한팀일때.

    void startButtonImageCho(String selectTeam, final Button[][] koreaChessButton, int b, int a){
        if(selectTeam.equals("cho")) {
            if(a == 0 && b == 0) {
                koreaChessButton[b][a].setText("choCha");
                koreaChessButton[b][a].setBackgroundResource(R.drawable.cho_cha);
            }else if(a == 1 && b == 0) {
                koreaChessButton[b][a].setText("choSang");
                koreaChessButton[b][a].setBackgroundResource(R.drawable.cho_sang);
            }else if(a == 2 && b == 0) {
                koreaChessButton[b][a].setText("choMa");
                koreaChessButton[b][a].setBackgroundResource(R.drawable.cho_ma);
            }else if(a == 3 && b == 0) {
                koreaChessButton[b][a].setText("choSa");
                koreaChessButton[b][a].setBackgroundResource(R.drawable.cho_sa);
            }else if(a == 5 && b == 0) {
                koreaChessButton[b][a].setText("choSa");
                koreaChessButton[b][a].setBackgroundResource(R.drawable.cho_sa);
            }else if(a == 6 && b == 0) {
                koreaChessButton[b][a].setText("choMa");
                koreaChessButton[b][a].setBackgroundResource(R.drawable.cho_ma);
            }else if(a == 7 && b == 0) {
                koreaChessButton[b][a].setText("choSang");
                koreaChessButton[b][a].setBackgroundResource(R.drawable.cho_sang);
            }else if(a == 8 && b == 0) {
                koreaChessButton[b][a].setText("choCha");
                koreaChessButton[b][a].setBackgroundResource(R.drawable.cho_cha);
            }else if(a == 1 && b == 2) {
                koreaChessButton[b][a].setText("choPho");
                koreaChessButton[b][a].setBackgroundResource(R.drawable.cho_pho);
            }else if(a == 7 && b == 2) {
                koreaChessButton[b][a].setText("choPho");
                koreaChessButton[b][a].setBackgroundResource(R.drawable.cho_pho);
            }else if(a == 4 && b == 1) {
                koreaChessButton[b][a].setText("choKing");
                koreaChessButton[b][a].setBackgroundResource(R.drawable.cho_king);
            }else if(a == 0 && b == 3) {
                koreaChessButton[b][a].setText("choJoll");
                koreaChessButton[b][a].setBackgroundResource(R.drawable.cho_joll);
            }else if(a == 2 && b == 3) {
                koreaChessButton[b][a].setText("choJoll");
                koreaChessButton[b][a].setBackgroundResource(R.drawable.cho_joll);
            }else if(a == 4 && b == 3) {
                koreaChessButton[b][a].setText("choJoll");
                koreaChessButton[b][a].setBackgroundResource(R.drawable.cho_joll);
            }else if(a == 6 && b == 3) {
                koreaChessButton[b][a].setText("choJoll");
                koreaChessButton[b][a].setBackgroundResource(R.drawable.cho_joll);
            }else if(a == 8 && b == 3) {
                koreaChessButton[b][a].setText("choJoll");
                koreaChessButton[b][a].setBackgroundResource(R.drawable.cho_joll);
            }else if(a == 0 && b == 9) {
                koreaChessButton[b][a].setText("hanCha");
                koreaChessButton[b][a].setBackgroundResource(R.drawable.han_cha);
            }else if(a == 1 && b == 9) {
                koreaChessButton[b][a].setText("hanSang");
                koreaChessButton[b][a].setBackgroundResource(R.drawable.han_sang);
            }else if(a == 2 && b == 9) {
                koreaChessButton[b][a].setText("hanMa");
                koreaChessButton[b][a].setBackgroundResource(R.drawable.han_ma);
            }else if(a == 3 && b == 9) {
                koreaChessButton[b][a].setText("hanSa");
                koreaChessButton[b][a].setBackgroundResource(R.drawable.han_sa);
            }else if(a == 5 && b == 9) {
                koreaChessButton[b][a].setText("hanSa");
                koreaChessButton[b][a].setBackgroundResource(R.drawable.han_sa);
            }else if(a == 6 && b == 9) {
                koreaChessButton[b][a].setText("hanMa");
                koreaChessButton[b][a].setBackgroundResource(R.drawable.han_ma);
            }else if(a == 7 && b == 9) {
                koreaChessButton[b][a].setText("hanSang");
                koreaChessButton[b][a].setBackgroundResource(R.drawable.han_sang);
            }else if(a == 8 && b == 9) {
                koreaChessButton[b][a].setText("hanCha");
                koreaChessButton[b][a].setBackgroundResource(R.drawable.han_cha);
            }else if(a == 1 && b == 7) {
                koreaChessButton[b][a].setText("hanPho");
                koreaChessButton[b][a].setBackgroundResource(R.drawable.han_pho);
            }else if(a == 7 && b == 7) {
                koreaChessButton[b][a].setText("hanPho");
                koreaChessButton[b][a].setBackgroundResource(R.drawable.han_pho);
            }else if(a == 4 && b == 8) {
                koreaChessButton[b][a].setText("hanKing");
                koreaChessButton[b][a].setBackgroundResource(R.drawable.han_king);
            }else if(a == 0 && b == 6) {
                koreaChessButton[b][a].setText("hanJoll");
                koreaChessButton[b][a].setBackgroundResource(R.drawable.han_joll);
            }else if(a == 2 && b == 6) {
                koreaChessButton[b][a].setText("hanJoll");
                koreaChessButton[b][a].setBackgroundResource(R.drawable.han_joll);
            }else if(a == 4 && b == 6) {
                koreaChessButton[b][a].setText("hanJoll");
                koreaChessButton[b][a].setBackgroundResource(R.drawable.han_joll);
            }else if(a == 6 && b == 6) {
                koreaChessButton[b][a].setText("hanJoll");
                koreaChessButton[b][a].setBackgroundResource(R.drawable.han_joll);
            }else if(a == 8 && b == 6) {
                koreaChessButton[b][a].setText("hanJoll");
                koreaChessButton[b][a].setBackgroundResource(R.drawable.han_joll);
            } else {
                koreaChessButton[b][a].setText("empty");
                koreaChessButton[b][a].setBackgroundResource(R.drawable.button_default_layout);
            }
        }
    }// 버튼 시작할때 초기화. 내가 초팀일때.

}
