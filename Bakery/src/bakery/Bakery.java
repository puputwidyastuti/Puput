/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package bakery;

/**
 *
 * @author acer
 */
import java.util.Scanner;
import java.io.*;
import java.time.Year;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Bakery {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException{
        // TODO code application logic here
    
        Scanner terminalInput = new Scanner(System.in);
        String pilihanUser;
        boolean isNext = true;

        while (isNext) {
            clearScreen();
            System.out.println("===============================");
            System.out.println("     Toko Kue Ice Bakery         ");
            System.out.println("================================");
            System.out.println("1.\tTampilkan");
            System.out.println("2.\tCari");
            System.out.println("3.\tTambah");
            System.out.println("4.\tUbah");
            System.out.println("5.\tHapus");
            System.out.println("================================");

            System.out.print("\n\nMasukkan Pilihan : ");
            pilihanUser = terminalInput.next();

            switch (pilihanUser) {
                case "1":
                    System.out.println("\n\"====================================================");
                    System.out.println("                        LIST                        ");
                    System.out.println("====================================================");
                    tampilData();
                    break;
                case "2":
                    System.out.println("\n===========");
                    System.out.println("    CARI   ");
                    System.out.println("===========");
                    cariData();
                    break;
                case "3":
                    System.out.println("\n================");
                    System.out.println("     TAMBAH       ");
                    System.out.println("================");
                    tambahData();
                    break;
                case "4":
                    System.out.println("\n==============");
                    System.out.println("       UBAH    ");
                    System.out.println("==============");
                    // ubah data
                    break;
                case "5":
                    System.out.println("\n===============");
                    System.out.println("    HAPUS       ");
                    System.out.println("===============");
                    // hapus data
                    break;
                default:
                    System.err.println("\nPilihan yang Anda masukkan tidak sesuai");
            }

            isNext = getYesorNo("Apakah Anda ingin melanjutkan");
        }
    }

    private static void tampilData() throws IOException{

        FileReader fileInput;
        BufferedReader bufferInput;

        try {
            fileInput = new FileReader("database.txt");
            bufferInput = new BufferedReader(fileInput);
        } catch (Exception e){
            System.err.println("Data tidak ditemukan");
            System.err.println("Silahkan tambahkan data terlebih dahulu");
            return;
        }


        System.out.println("\n| No |\tCode      |\tVarian                |\tStok");
        System.out.println("====================================================");

        String data = bufferInput.readLine();
        int nomorData = 0;
        while(data != null) {
            nomorData++;

            StringTokenizer stringToken = new StringTokenizer(data, ",");

            stringToken.nextToken();
            System.out.printf("| %2d ", nomorData);
            System.out.printf("|\t%4s  ", stringToken.nextToken());
            System.out.printf("|\t%20s  ", stringToken.nextToken());
//            System.out.printf("|\t%-20s  ", stringToken.nextToken());
            System.out.printf("|\t%s   ", stringToken.nextToken());
            System.out.print("\n");

            data = bufferInput.readLine();
        }

        System.out.println("\n====================================================");
    }

    private static void cariData() throws IOException{

        // membaca database ada atau tidak

        try {
            File file = new File("database.txt");
        } catch (Exception e){
            System.err.println("Data Tidak ditemukan");
            System.err.println("Silahkan tambah data terlebih dahulu");
            return;
        }

        // kita ambil keyword dari user

        Scanner terminalInput = new Scanner(System.in);
        System.out.print("Masukan code :  ");
        String cariString = terminalInput.nextLine();
        String[] keywords = cariString.split("\\s+");

        // kita cek keyword di database
        //cekDiDatabase(keywords,true);

    }

    private static void tambahData() throws IOException{


        FileWriter fileOutput = new FileWriter("database.txt",true);
        BufferedWriter bufferOutput = new BufferedWriter(fileOutput);


        // mengambil input dari user
        Scanner terminalInput = new Scanner(System.in);
        String code, varian,stok;

        System.out.print("masukan nama code: ");
        code = terminalInput.nextLine();
        System.out.print("masukan varian cake: ");
        varian = terminalInput.nextLine();
        System.out.print("masukan stok/jumlah Cake yang tersedia,  format : (YYYY): ");
        stok = ambilJumlah();

        // cek buku di database

        String[] keywords = {stok+","+code+","+varian};
        System.out.println(Arrays.toString(keywords));

        boolean isExist = cekDataDiDatabase(keywords,false);

        // menulis buku di databse
        if (!isExist){
//            fiersabesari_2012_1,2012,fiersa besari,media kita,jejak langkah
            System.out.println(ambilEntryPerTahun(code, stok));
            long nomorEntry = ambilEntryPerTahun(code, stok) + 1;

            String varianTanpaSpasi = code.replaceAll("\\s+","");
            String primaryKey = varianTanpaSpasi+"_"+stok+"_"+nomorEntry;
            System.out.println("\nData yang ingin dimasukkan : ");
            System.out.println("\n=============================================");
        //    System.out.println("primary key  : " + primaryKey);
            System.out.println("stok  : " + stok);
            System.out.println("code      : " + code);
            System.out.println("varian        : " + varian);
            

            boolean isTambah = getYesorNo("Apakah Anda ingin menambah data tersebut? ");

            if(isTambah){
                bufferOutput.write(primaryKey + "," + stok + ","+ code +","+varian);
                bufferOutput.newLine();
                bufferOutput.flush();
            }

        } else {
            System.out.println("Data berhasil ditambahkan:");
            cekDataDiDatabase(keywords,true);
        }


        bufferOutput.close();
    }
    
    private static void ubahData() throws IOException{
        // kita ambil database original
        File database = new File("database.txt");
        FileReader fileInput = new FileReader(database);
        BufferedReader bufferedInput = new BufferedReader(fileInput);

        // kita buat database sementara
        File tempDB = new File("tempDB.txt");
        FileWriter fileOutput = new FileWriter(tempDB);
        BufferedWriter bufferedOutput = new BufferedWriter(fileOutput);

        // tampilkan data
        System.out.println("List Buku");
        tampilData();
        Scanner terminalInput = new Scanner(System.in);
        System.out.print("\nMasukan code yang akan diubah : ");
        int updateNum = terminalInput.nextInt();

        String data = bufferedInput.readLine();
        int entryCounts = 0;

        while (data != null){
            entryCounts++;

            StringTokenizer st = new StringTokenizer(data,",");
            if (updateNum == entryCounts){
                System.out.println("\nData yang ingin anda ubah adalah:");
                System.out.println("===================================");
                System.out.println("Code                : " + st.nextToken());
                System.out.println("Varian              : " + st.nextToken());
                System.out.println("Stok                : " + st.nextToken());

                // update data

                // mengambil input dari user

                String[] fieldData = {"code","varian","stok"};
                String[] tempData = new String[4];

                st = new StringTokenizer(data,",");
                String originalData = st.nextToken();

                for(int i=0; i < fieldData.length ; i++) {
                    boolean isUpdate = getYesorNo("apakah anda ingin mengubah " + fieldData[i]);
                    originalData = st.nextToken();
                    if (isUpdate){
                        //user input

                        if (fieldData[i].equalsIgnoreCase("code")){
                            System.out.print("masukan code, format=(YYYY): ");
                        } else {
                            terminalInput = new Scanner(System.in);
                            System.out.print("\nMasukan " + fieldData[i] + " baru: ");
                            tempData[i] = terminalInput.nextLine();
                        }

                    } else {
                        tempData[i] = originalData;
                    }
                }

                // tampilkan data baru ke layar
                st = new StringTokenizer(data,",");
                st.nextToken();
                System.out.println("\nData terbaru adalah ");
                System.out.println("================================");
                System.out.println("code               : " + st.nextToken() + " --> " + tempData[0]);
                System.out.println("varian             : " + st.nextToken() + " --> " + tempData[1]);
                System.out.println("stok               : " + st.nextToken() + " --> " + tempData[2]);


                boolean isUpdate = getYesorNo("apakah anda yakin ingin mengupdate data tersebut");

                if (isUpdate){

                    // cek data baru di database
                    boolean isExist = cekDataDiDatabase(tempData,false);

                    if(isExist){
                        System.err.println("data sudah ada, proses update dibatalkan, \nsilahkan delete data yang bersangkutan");
                        bufferedOutput.write(data);

                    } else {
                        String code = tempData[0];
                        String varian = tempData[1];
                        String stok = tempData[2];

                        long nomorEntry = ambilEntryPerTahun(varian, code) + 1;

                        String varianTanpaSpasi = varian.replaceAll("\\s+","");
                        String primaryKey = varianTanpaSpasi+"_"+code+"_"+nomorEntry;

                        // tulis data ke database
                        bufferedOutput.write(primaryKey + "," + code + ","+ varian +"," + "stok ");
                    }
                } else {
                    // copy data
                    bufferedOutput.write(data);
                }
            } else {
                // copy data
                bufferedOutput.write(data);
            }
            bufferedOutput.newLine();

            data = bufferedInput.readLine();
        }

        // menulis data ke file
        bufferedOutput.flush();

        // delete original database
        database.delete();
        // rename file tempDB menjadi database
        tempDB.renameTo(database);

    }
    
    private static void hapusData() throws  IOException{
        File database = new File("database.txt");
        FileReader fileInput = new FileReader(database);
        BufferedReader bufferedInput = new BufferedReader(fileInput);
        File tempDB = new File("tempDB.txt");
        FileWriter fileOutput = new FileWriter(tempDB);
        BufferedWriter bufferedOutput = new BufferedWriter(fileOutput);

        System.out.println("List Data");
        tampilData();

        Scanner terminalInput = new Scanner(System.in);
        System.out.print("\nMasukan code yang akan dihapus: ");
        int deleteNum = terminalInput.nextInt();
        boolean isFound = false;
        int entryCounts = 0;

        String data = bufferedInput.readLine();

        while (data != null){
            entryCounts++;
            boolean isDelete = false;

            StringTokenizer st = new StringTokenizer(data,",");

            // tampilkan data yang ingin di hapus
            if (deleteNum == entryCounts){
                System.out.println("\nData yang ingin di hapus adalah:");
                System.out.println("==================================");
                System.out.println("code         : " + st.nextToken());
                System.out.println("varian       : " + st.nextToken());
                System.out.println("stok         : " + st.nextToken());

                isDelete = getYesorNo("Apakah anda yakin akan menghapus?");
                isFound = true;
            }

            if(isDelete){
                System.out.println("Data berhasil dihapus");
            } else {

                bufferedOutput.write(data);
                bufferedOutput.newLine();
            }
            data = bufferedInput.readLine();
        }

        if(!isFound){
            System.err.println("Data tidak ditemukan");
        }

        bufferedOutput.flush();
        database.delete();
        tempDB.renameTo(database);

    }

    private static long ambilEntryPerTahun(String code, String stok) throws IOException {
        FileReader fileInput = new FileReader("database.txt");
        BufferedReader bufferInput = new BufferedReader(fileInput);

        long entry = 0;
        String data = bufferInput.readLine();
        Scanner dataScanner;
        String primaryKey;

        while(data != null){
            dataScanner = new Scanner(data);
            dataScanner.useDelimiter(",");
            primaryKey = dataScanner.next();
            dataScanner = new Scanner(primaryKey);
            dataScanner.useDelimiter("_");

            code = code.replaceAll("\\s+","");

            if (code.equalsIgnoreCase(dataScanner.next()) && stok.equalsIgnoreCase(dataScanner.next()) ) {
                entry = dataScanner.nextInt();
            }

            data = bufferInput.readLine();
        }

        return entry;
    }

    private static boolean cekDataDiDatabase(String[] keywords, boolean isDisplay) throws IOException{

        FileReader fileInput = new FileReader("database.txt");
        BufferedReader bufferInput = new BufferedReader(fileInput);

        String data = bufferInput.readLine();
        boolean isExist = false;
        int nomorData = 0;

        if (isDisplay) {
            System.out.println("\n| No |\tTahun |\tPenulis                |\tPenerbit               |\tJudul Buku");
            System.out.println("=================================================================================");
        }

        while(data != null){
            isExist = true;
            for(String keyword:keywords){
                isExist = isExist && data.toLowerCase().contains(keyword.toLowerCase());
            }
            if(isExist){
                if(isDisplay) {
                    nomorData++;
                    StringTokenizer stringToken = new StringTokenizer(data, ",");

                    stringToken.nextToken();
                    System.out.printf("| %2d ", nomorData);
                    System.out.printf("|\t%4s  ", stringToken.nextToken());
                    System.out.printf("|\t%-20s   ", stringToken.nextToken());
                    System.out.printf("|\t%s  ()); ", stringToken.nextToken());
                    System.out.print("\n");
                } else {
                    break;
                }
            }

            data = bufferInput.readLine();
        }

        if (isDisplay){
            System.out.println("----------------------------------------------------------------------------------------------------------");
        }

        return isExist;
    }

    private static String ambilJumlah() throws IOException{
        boolean stokValid = false;
        Scanner terminalInput = new Scanner(System.in);
        String stokInput = terminalInput.nextLine();
        while(!stokValid) {
            try {
                Year.parse(stokInput);
                stokValid = true;
            } catch (Exception e) {
                System.out.println("Format stok yang anda masukan salah, format=(YYYY)");
                System.out.print("silahkan masukkan stok : ");
                stokValid = false;
                stokInput = terminalInput.nextLine();
            }
        }

        return stokInput;
    }

    private static boolean getYesorNo(String message){
        Scanner terminalInput = new Scanner(System.in);
        System.out.print("\n"+message+" (y/n)? ");
        String pilihanUser = terminalInput.next();

        while(!pilihanUser.equalsIgnoreCase("y") && !pilihanUser.equalsIgnoreCase("n")) {
            System.err.println("Pilihan yang anda masukkan salah");
            System.out.print("\n"+message+" (y/n)? ");
            pilihanUser = terminalInput.next();
        }

        return pilihanUser.equalsIgnoreCase("y");

    }

    private static void clearScreen(){
        try {
            if (System.getProperty("os.name").contains("Windows")){
                new ProcessBuilder("cmd","/c","cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033\143");
            }
        } catch (Exception ex){
            System.err.println("Data tidak dapat dihilangkan");
        }
    }
}


