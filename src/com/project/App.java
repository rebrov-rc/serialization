package com.project;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;


public class App {


    private static void saveGame(String path, GameProgress game ){


        try(FileOutputStream file = new FileOutputStream( path );
            ObjectOutputStream object = new ObjectOutputStream(file); ){

            object.writeObject( game );

        } catch ( IOException ex ) {

            System.err.println( ex );

        }
    }

    private static <T> T openProgress( String path ){



        try( FileInputStream file = new FileInputStream( path);
             ObjectInputStream object = new ObjectInputStream(file); ){


            return (T) object.readObject();


        }catch ( FileNotFoundException ex ){
            ex.getStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }catch ( IOException ex ){
            ex.getStackTrace();
        }catch(Exception exception){
            System.out.println(exception.toString());
        }

        return  null;
    }

    private static void zipFiles(String path, String wh){
        try(ZipOutputStream zip = new ZipOutputStream(new FileOutputStream(path));
            FileInputStream fil = new FileInputStream(wh)){

            ZipEntry entry = new ZipEntry("notes.txt");
            zip.putNextEntry(entry);

            byte [] buffer = new byte [fil.available()];
            fil.read(buffer);
            zip.write(buffer);
            zip.closeEntry();

        }catch( Exception ex  ){
            System.out.println(ex.getMessage());
        }
    }

    private static void openZip( String pathOut, String pathIn ){


        try (FileInputStream file = new FileInputStream(pathOut); ZipInputStream zin = new ZipInputStream(file)){


            while (  zin.getNextEntry()  != null ){

                FileOutputStream fout = new FileOutputStream(pathIn);

                for (  int c = zin.read()  ; c !=-1; c = zin.read() ){
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

    public void run(){

        StringBuilder str = new StringBuilder("");

        File rootDir = new File("C:\\Users\\rebro\\Desktop\\WORK");
        File games = new File(rootDir + "\\Games");

        File[] gamesInnerDir = new File[10];
        File[] srcInnerDir = new File[10];
        File[] resInnerDir = new File[10];

        games.mkdir();

        ////////////////////////////////////
        // Games  создание директорий.
        if (games.isDirectory()) {

            str.append(NotifyUtils.DIR_CREATED + games + "\n");

            gamesInnerDir[0] = new File(games + "\\src");
            gamesInnerDir[1] = new File(games + "\\res");
            gamesInnerDir[2] = new File(games + "\\savegames");
            gamesInnerDir[3] = new File(games + "\\temp");


            for (File el : gamesInnerDir) {

                try {
                    el.mkdir();


                    if (el.isDirectory()) {

                        str.append(NotifyUtils.DIR_CREATED + el + "\n");

                    } else {

                        str.append(NotifyUtils.DIR_ERROR + games + "\n");


                    }
                } catch (NullPointerException ex) {
                }
            }


        } else {

        }

        ////////////////////////////////////
        // Games.src  создание директорий.
        if (gamesInnerDir[0].isDirectory()) {

            srcInnerDir[0] = new File(gamesInnerDir[0] + "\\main");
            srcInnerDir[1] = new File(gamesInnerDir[0] + "\\test");

            for (File el : srcInnerDir) {

                try {

                    el.mkdir();


                    if (el.isDirectory()) {

                        str.append(NotifyUtils.DIR_CREATED + el + "\n");

                    } else {

                        str.append(NotifyUtils.DIR_ERROR + el + "\n");

                    }
                } catch (NullPointerException ex) {

                }

            }

        } else {

        }


        ////////////////////////////////////
        // Games.src.main  создание файлов.
        if (srcInnerDir[0].isDirectory()) {

            File main = new File(srcInnerDir[0], "Main.java");
            File utils = new File(srcInnerDir[0], "Utils.java");

            try {

                boolean res = main.createNewFile();

                if (res) {

                    str.append(NotifyUtils.FILE_CREATED + main + "\n");

                } else {

                    str.append(NotifyUtils.FILE_NO_CREATED + main + "\n");
                }

            } catch (IOException exception) {

                str.append(NotifyUtils.FILE_ERROR + main + "\n");

            }

            try {

                boolean res = utils.createNewFile();

                if (res) {

                    str.append(NotifyUtils.FILE_CREATED + utils + "\n");

                } else {

                    str.append(NotifyUtils.FILE_NO_CREATED + utils + "\n");

                }

            } catch (IOException exception) {

                str.append(NotifyUtils.FILE_ERROR + utils + "\n");

            }
        } else {

        }


        ////////////////////////////////////
        // Games.res  создание директорий.
        if (gamesInnerDir[1].isDirectory()) {

            resInnerDir[0] = new File(gamesInnerDir[1] + "\\drawables");
            resInnerDir[1] = new File(gamesInnerDir[1] + "\\vectors");
            resInnerDir[2] = new File(gamesInnerDir[1] + "\\icons");

            for (File el : resInnerDir) {

                try {

                    el.mkdir();

                    if (el.isDirectory()) {

                        str.append(NotifyUtils.DIR_CREATED + el + "\n");

                    } else {

                        str.append(NotifyUtils.DIR_ERROR + el + "\n");

                    }
                } catch (NullPointerException ex) {

                }

            }

        } else {

        }


        ////////////////////////////////////
        // Games.temp  создание файлов.
        if (gamesInnerDir[3].isDirectory()) {

            File temp = new File(gamesInnerDir[3], "temp.txt");


            try {

                boolean res = temp.createNewFile();

                if (res) {

                    str.append(NotifyUtils.FILE_CREATED + temp + "\n");

                } else {

                    str.append(NotifyUtils.FILE_NO_CREATED + temp + "\n");

                }

            } catch (IOException exception) {

                str.append(NotifyUtils.FILE_ERROR + temp + "\n");

            }

            try(FileWriter writer = new FileWriter(temp)){

                writer.write(str.toString());
                writer.flush();

            }catch(IOException exception){
                System.out.println(exception.getMessage());
            }



        } else {

            System.out.println(str.toString());

        }


        ////////////////////////////////////
        //  Сериализация объектов

        GameProgress game1 = new GameProgress(10, 30, 2, 3.54);
        GameProgress game2 = new GameProgress(50, 10, 6, 5.98);
        GameProgress game3 = new GameProgress(100, 50, 7, 8.46);

        saveGame( gamesInnerDir[2] + "\\game1.dat", game1 );
        saveGame( gamesInnerDir[2] + "\\game2.dat", game2 );
        saveGame( gamesInnerDir[2] + "\\game3.dat", game3 );

        zipFiles( gamesInnerDir[2] + "\\game1.zip", gamesInnerDir[2] + "\\game1.dat" );
        zipFiles( gamesInnerDir[2] + "\\game2.zip", gamesInnerDir[2] + "\\game2.dat" );
        zipFiles( gamesInnerDir[2] + "\\game3.zip", gamesInnerDir[2] + "\\game3.dat" );


        ////////////////////////////////////
        //  Архивация объектов и удаление объектов *.dat

        File gm1 = new File(gamesInnerDir[2] + "\\game1.dat" );
        File gm2 = new File(gamesInnerDir[2] + "\\game2.dat" );
        File gm3 = new File(gamesInnerDir[2] + "\\game3.dat" );

        gm1.delete();
        gm2.delete();
        gm3.delete();


        ////////////////////////////////////
        //  Разархивация объектов и вывод в консоле
        openZip(gamesInnerDir[2] + "\\game2.zip", gamesInnerDir[2] + "\\game4.dat" );


        ////////////////////////////////////
        //  Десериализация файла
        GameProgress gameOut =  openProgress(gamesInnerDir[2] + "\\game4.dat");


        ////////////////////////////////////
        //  Вывод объекта в консоль.
        System.out.println(gameOut.toString());
    }
}
