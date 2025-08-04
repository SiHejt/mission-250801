package com.back;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        WiseSayingRepository wiseSayingRepository = new WiseSayingRepository();
        List<WiseSaying> sayings = wiseSayingRepository.loadAll();

        System.out.println("== 명언 앱 ==");

        while (true) {
            System.out.print("명령) ");
            String cmd = sc.nextLine();

            if (cmd.equals("종료")) break;

            if (cmd.equals("등록")) {
                System.out.print("명언 : ");
                String content = sc.nextLine();
                System.out.print("작가 : ");
                String author = sc.nextLine();

                WiseSaying ws = new WiseSaying(content, author);
                sayings.add(ws);
                wiseSayingRepository.save(ws);
                System.out.println(ws.id + "번 명언이 등록되었습니다.");

            } else if (cmd.equals("목록")) {
                System.out.println("번호 / 작가 / 명언");
                System.out.println("----------------------");
                for (int i = sayings.size() - 1; i >= 0; i--) {
                    WiseSaying ws = sayings.get(i);
                    System.out.println(ws.id + " / " + ws.author + " / " + ws.content);
                }

            } else if (cmd.startsWith("삭제?id=")) {
                int id = Integer.parseInt(cmd.substring(6));
                boolean found = false;
                for (int i = 0; i < sayings.size(); i++) {
                    if (sayings.get(i).id == id) {
                        sayings.remove(i);
                        wiseSayingRepository.delete(id);
                        System.out.println(id + "번 명언이 삭제되었습니다.");
                        found = true;
                        break;
                    }
                }
                if (!found) System.out.println(id + "번 명언은 존재하지 않습니다.");

            } else if (cmd.startsWith("수정?id=")) {
                int id = Integer.parseInt(cmd.substring(6));
                WiseSaying target = null;

                for (WiseSaying ws : sayings) {
                    if (ws.id == id) {
                        target = ws;
                        break;
                    }
                }

                if (target == null) {
                    System.out.println(id + "번 명언은 존재하지 않습니다.");
                } else {
                    System.out.println("명언(기존) : " + target.content);
                    System.out.print("명언 : ");
                    target.content = sc.nextLine();

                    System.out.println("작가(기존) : " + target.author);
                    System.out.print("작가 : ");
                    target.author = sc.nextLine();

                    wiseSayingRepository.save(target);
                    System.out.println(id + "번 명언이 수정되었습니다.");
                }

            } else if (cmd.equals("빌드")) {
                wiseSayingRepository.buildDataJson(sayings);
            } else {
                System.out.println("잘못된 명령어 입니다.");
            }
        }

        sc.close();
    }
}
