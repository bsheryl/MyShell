package MyShellForWindows;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Terminal {
    private String currentDirectory;

    public Terminal(String currentDirectory) {
        System.setProperty("user.dir", currentDirectory);
        this.currentDirectory = System.getProperty("user.dir");
    }

    public String getCurrentDirectory() {
        return currentDirectory;
    }

    public void commandDir(String[] commands) {
        List<Map<String, String>> mapList = new ArrayList<>();
        File dir;
        int d = 0;
        int h = 0;
        for (int i = 0; i < commands.length; ++i) {
            if (commands[i].equalsIgnoreCase("/a:-d")) {
                d = -1;
            } else if (commands[i].equalsIgnoreCase("/a:h")) {
                h = 1;
            }
        }
        if (commands.length > 1 && commands[1].contains(":\\")) {
            if (Files.notExists(Path.of(commands[1]), new LinkOption[]{LinkOption.NOFOLLOW_LINKS})) {
                System.out.println("Файл не найден");
                return;
            }
            dir = new File(commands[1]);
        } else {
            dir = new File(currentDirectory);
        }
        int countFiles = 0;
        int countDir = 0;
        long totalSize = 0;
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        if (dir.isDirectory()) {
            for (File item : dir.listFiles()) {
                Map<String, String> mapItem = new HashMap<>();
                if (!item.isHidden() && h == 1) continue;
                if (item.isDirectory()) {
                    if (d == -1 ) continue;
                    mapItem.put("dir", "<DIR>");
                    mapItem.put("size", "");
                    ++countDir;
                } else {
                    mapItem.put("dir", "\t");
                    mapItem.put("size", Long.toString(item.length()));
                    totalSize += item.length();
                    ++countFiles;
                }
                LocalDateTime date = LocalDateTime.ofInstant(Instant.ofEpochMilli(item.lastModified()), ZoneId.systemDefault());
                mapItem.put("date", dateFormatter.format(date));
                mapItem.put("time", timeFormatter.format(date));
                mapItem.put("name", item.toString().substring(item.toString().lastIndexOf("\\") + 1));
                mapList.add(mapItem);
            }
            for (Map<String, String> map : mapList) {
                System.out.printf("%s\t%s\t%s\t%12s\t%s\n",
                        map.get("date"), map.get("time"), map.get("dir"), map.get("size"), map.get("name"));
            }
            System.out.println("\t\t\t" + countFiles + " файлов\t" + totalSize + " байт");
            System.out.println("\t\t\t" + countDir + " папок");
        }
    }

    public void commandSort(String[] commands) throws IOException {
        List<String> list = new ArrayList<>();
        if (commands.length == 1 || (commands.length == 2 && commands[1].equalsIgnoreCase("/r"))) {
            list = readStandartInput();
        } else if (commands.length == 2 && !commands[1].equalsIgnoreCase("/r")) {
            list = readFile(commands[1]);
        } else if (commands.length == 3) {
            list = readFile(commands[2]);
        }
        Collections.sort(list);
        if (commands.length > 1 && commands[1].equalsIgnoreCase("/r")) {
            Collections.reverse(list);
        }
        for (String str : list) {
            System.out.println(str);
        }
    }

    private List<String> readStandartInput() {
        Scanner scanner1 = new Scanner(System.in);
        List<String> list = new ArrayList<>();
        while (true) {
            String input = scanner1.nextLine();
            if (input.equalsIgnoreCase("exit")) {
                break;
            }
            list.add(input);
        }
        return list;
    }

    private List<String> readFile(String fileName) throws IOException {
        List<String> list = new ArrayList<>();
        FileInputStream fileInputStream = new FileInputStream(fileName);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
            list.add(line);
        }
        return list;
    }

    public void commandMkdir(String[] commands) {
        File catalog = new File(commands[1]);
        catalog.mkdir();
    }

    public void commandDate() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDateTime localDateTime = LocalDateTime.now();
        Scanner scanner = new Scanner(System.in);
        Date date = new Date();
        System.out.println("Текущая дата: " + dateFormatter.format(localDateTime));
        System.out.print("Введите новую дату (дд-мм-гг): ");
        String line = scanner.nextLine();
    }
}
