package com.back;

import java.util.List;
import java.util.Scanner;

public class App {
    WiseSayingRepository wiseSayingRepository = new WiseSayingRepository();
    Scanner sc = new Scanner(System.in);
    List<WiseSaying> sayings = wiseSayingRepository.loadAll();
    public void run() {

        System.out.println("== 명언 앱 ==");

        while (true) {
            System.out.print("명령) ");
            String cmd = sc.nextLine();

            if (cmd.equals("종료")) break;

            if (cmd.equals("등록")) {
                handleRegister();
            } else if (cmd.equals("목록")) {
                handleList();
            } else if (cmd.startsWith("삭제?id=")) {
                handleDelete(cmd);
            } else if (cmd.startsWith("수정?id=")) {
                handleUpdate(cmd);
            } else if (cmd.equals("빌드")) {
                wiseSayingRepository.buildDataJson(sayings);
            } else {
                System.out.println("잘못된 명령어 입니다.");
            }
        }

        sc.close();
    }

    private void handleRegister() {
        System.out.print("명언 : ");
        String content = sc.nextLine();
        System.out.print("작가 : ");
        String author = sc.nextLine();

        WiseSaying wiseSaying = new WiseSaying(content, author);
        sayings.add(wiseSaying);
        wiseSayingRepository.save(wiseSaying);
        System.out.println(wiseSaying.id + "번 명언이 등록되었습니다.");
    }
    private void handleList() {
        System.out.println("번호 / 작가 / 명언");
        System.out.println("----------------------");
        for (int i = sayings.size() - 1; i >= 0; i--) {
            WiseSaying wiseSaying = sayings.get(i);
            System.out.println(wiseSaying.id + " / " + wiseSaying.author + " / " + wiseSaying.content);
        }
    }
    private void handleDelete(String cmd) {
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
    }
    private void handleUpdate(String cmd) {

        int id = Integer.parseInt(cmd.substring(6));
        WiseSaying target = null;

        for (WiseSaying wiseSaying : sayings) {
            if (wiseSaying.id == id) {
                target = wiseSaying;
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
    }
}
