package de.exit13.utils;

import de.exit13.utils.Configuration.Config;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by dude on 13.03.15.
 */
public class ESImportUtils {
    public void elasticImport()  throws Exception{
        System.out.println(Config.ANSI_RED_FG + "elastic" + Config.ANSI_RESET +" import...");
        FileUtils fu = new FileUtils();

        String directory = "/data/rawdata/CLIMAT";
        String filter = "CLIMAT_RAW_200303.txt";
        ArrayList<String> fileList = fu.readDirectory(directory, filter);
        System.out.println("Read " + fileList.size() + " files from " + directory + ".");

        String header = "year;month;IIiii;G1;Po;G1;P;G1;sn;T;st;G1;sn;Tx;sn;Tn;G1;e;G1;R1;Rd;nr;G1;S1;ps;G1;mp;mT;mTx;mTn;G1;me;mR;mS;G2;Yb;Yc;G2;Po;G2;P;G2;sn;T;st;G2;sn;Tx;sn;Tn;G2;e;G2;R1;nr;G2;S1;G2;YP;YT;YTx;G2;Ye;YR;YS;G3;T25;T30;G3;T35;T40;G3;Tn0;Tx0;G3;R01;R05;G3;R10;R50;G3;R100;R150;G3;s00;s01;G3;s10;s50;G3;f10;f20;f30;G3;V1;V2;V3;G4;sn;Txd;yx;G4;sn;Tnd;yn;G4;sn;Tax;yax;G4;sn;Tan;yan;G4;Rx;yr;G4;iw;fx;yfx;G4;Dts;Dgr;G4;iy;Gx;Gn";
        String[] pieces = header.split(";");
        for (int i=0; i< pieces.length; i++) {
            System.out.println(i + "\t\t--\t\t" + pieces[i] + "\t\t--\t\t\t");
        }

        // FORMAT:
        // year;month;IIiii;  --  year;month;station_id;
        // G1;Po;  --
        // G1;P;
        // G1;sn;T;st;
        // G1;sn;Tx;sn;Tn;
        // G1;e;
        // G1;R1;Rd;nr;
        // G1;S1;ps;
        // G1;mp;mT;mTx;mTn;
        // G1;me;mR;mS
        // G1;P0;G1;P;G1;sign_of_follwing_field;mean_air_temp_per_month;
        System.out.println(Config.ANSI_GREEN_FG + "Done!" + Config.ANSI_RESET);
    }
}
