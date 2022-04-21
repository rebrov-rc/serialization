package com.project;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;


public class App {

    private StringBuilder str = new StringBuilder("");


    private void saveGame(String path, GameProgress game) {


        try (FileOutputStream file = new FileOutputStream(path);
             ObjectOutputStream object = new ObjectOutputStream(file);) {

            object.writeObject(game);

        } catch (IOException ex) {

            System.err.println(ex);

        }
    }

    private <T> T openProgress(String path) {


        try (FileInputStream file = new FileInputStream(path);
             ObjectInputStream object = new ObjectInputStream(file);) {


            return (T) object.readObject();


        } catch (FileNotFoundException ex) {
            ex.getStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException ex) {
            ex.getStackTrace();
        } catch (Exception exception) {
            System.out.println(exception.toString());
        }

        return null;
    }

    private void zipFiles(String path, String wh) {
        try (ZipOutputStream zip = new ZipOutputStream(new FileOutputStream(path));
             FileInputStream fil = new FileInputStream(wh)) {

            ZipEntry entry = new ZipEntry("notes.txt");
            zip.putNextEntry(entry);

            byte[] buffer = new byte[fil.available()];
            fil.read(buffer);
            zip.write(buffer);
            zip.closeEntry();

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void openZip(String pathOut, String pathIn) {


        try (FileInputStream file = new FileInputStream(pathOut); ZipInputStream zin = new ZipInputStream(file)) {


            while (zin.getNextEntry() != null) {

                FileOutputStream fout = new FileOutputStream(pathIn);

                for (int c = zin.read(); c != -1; c = zin.read()) {
                    fout.write(c);
                }

                fout.flush();
                fout.close();
                zin.closeEntry();


            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException exception) {
            exception.printStackTrace();
        }


    }

    private void dirCreation(File root, File[] dirs) {

        if (root.isDirectory()) {


            for (File el : dirs) {

                try {
                    el.mkdir();


                    if (el.isDirectory()) {

                        str.append(NotifyUtils.DIR_CREATED + el + "\n");

                    } else {

                        str.append(NotifyUtils.DIR_ERROR + root + "\n");


                    }
                } catch (NullPointerException ex) {
                }
            }


        } else {
            str.append(NotifyUtils.DIR_NO_EXIST + root.toString());
        }

    }

    private File fileCreation(File root, String fileName) {
        if (root.isDirectory()) {
            File file = new File(root, fileName);

            try {

                boolean res = file.createNewFile();

                if (res) {

                    str.append(NotifyUtils.FILE_CREATED + file + "\n");

                } else {

                    str.append(NotifyUtils.FILE_NO_CREATED + file + "\n");
                }

                return file;

            } catch (IOException exception) {

                str.append(NotifyUtils.FILE_ERROR + file + "\n");

            }

        } else {
            str.append(NotifyUtils.DIR_NO_EXIST + root.toString());
        }

        return null;
    }

    private boolean writeToFile(File file, String inner){
        try (FileWriter writer = new FileWriter(file)) {

            writer.write(inner);
            writer.flush();

        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
        return true;
    }

    public void run() {


        File rootDir = new File(DirStorage.ROOT);
        File gamesDir = new File(rootDir + DirStorage.GAMES);

        File[] gamesInnerDir = new File[10];
        File[] srcInnerDir = new File[10];
        File[] resInnerDir = new File[10];


        this.dirCreation(rootDir, new File[]{gamesDir});




        ////////////////////////////////////
        // Games  создание директорий.

        gamesInnerDir[0] = new File(gamesDir + "\\src");
        gamesInnerDir[1] = new File(gamesDir + "\\res");
        gamesInnerDir[2] = new File(gamesDir + "\\savegames");
        gamesInnerDir[3] = new File(gamesDir + "\\temp");

        this.dirCreation(gamesDir, gamesInnerDir);


        ////////////////////////////////////
        // Games.src  создание директорий.

        srcInnerDir[0] = new File(gamesInnerDir[0] + "\\main");
        srcInnerDir[1] = new File(gamesInnerDir[0] + "\\test");

        this.dirCreation(gamesInnerDir[0], srcInnerDir);



        ////////////////////////////////////
        // Games.src.main  создание файлов.

        this.fileCreation(srcInnerDir[0], DirStorage.FILES.MAIN);
        this.fileCreation(srcInnerDir[0], DirStorage.FILES.UTILS);



        ////////////////////////////////////
        // Games.res  создание директорий.

        resInnerDir[0] = new File(gamesInnerDir[1] + "\\drawables");
        resInnerDir[1] = new File(gamesInnerDir[1] + "\\vectors");
        resInnerDir[2] = new File(gamesInnerDir[1] + "\\icons");

        this.dirCreation(gamesInnerDir[1], resInnerDir);




        ////////////////////////////////////
        // Games.temp  создание файлов.
        File temp = this.fileCreation(gamesInnerDir[3], DirStorage.FILES.TEMP);
        this.writeToFile(temp, str.toString());


        ////////////////////////////////////
        //  Сериализация объектов

        GameProgress game1 = new GameProgress(10, 30, 2, 3.54);
        GameProgress game2 = new GameProgress(50, 10, 6, 5.98);
        GameProgress game3 = new GameProgress(100, 50, 7, 8.46);

        saveGame(gamesInnerDir[2] + "\\game1.dat", game1);
        saveGame(gamesInnerDir[2] + "\\game2.dat", game2);
        saveGame(gamesInnerDir[2] + "\\game3.dat", game3);

        zipFiles(gamesInnerDir[2] + "\\game1.zip", gamesInnerDir[2] + "\\game1.dat");
        zipFiles(gamesInnerDir[2] + "\\game2.zip", gamesInnerDir[2] + "\\game2.dat");
        zipFiles(gamesInnerDir[2] + "\\game3.zip", gamesInnerDir[2] + "\\game3.dat");


        ////////////////////////////////////
        //  Архивация объектов и удаление объектов *.dat

        File gm1 = new File(gamesInnerDir[2] + "\\game1.dat");
        File gm2 = new File(gamesInnerDir[2] + "\\game2.dat");
        File gm3 = new File(gamesInnerDir[2] + "\\game3.dat");

        gm1.delete();
        gm2.delete();
        gm3.delete();


        ////////////////////////////////////
        //  Разархивация объектов и вывод в консоле
        openZip(gamesInnerDir[2] + "\\game2.zip", gamesInnerDir[2] + "\\game4.dat");


        ////////////////////////////////////
        //  Десериализация файла
        GameProgress gameOut = openProgress(gamesInnerDir[2] + "\\game4.dat");


        ////////////////////////////////////
        //  Вывод объекта в консоль.
        System.out.println(gameOut.toString());
    }
}
