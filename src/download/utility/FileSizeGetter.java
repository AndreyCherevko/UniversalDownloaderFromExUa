package download.utility;

/**
 * Created by Андрей on 12.12.2014.
 */
public class FileSizeGetter {

    public static String getSize(int size){
        String outsize = null;
        if((int)(size/Math.pow(10,9))>1) {
            double newsize = size/Math.pow(10,9);
            outsize= newsize+" Gb";
        }
        else if((int)(size/Math.pow(10,6))>1) {
            double newsize = size/Math.pow(10,6);
            outsize = newsize+" Mb";
        }
        else if(((int)size/Math.pow(10,3))>1) {
            double newsize = size/Math.pow(10,3);
            outsize = newsize+" Kb";
        }
        else{
            outsize = size+" b";
        }
        int index = outsize.lastIndexOf(".");
        if(index>0)
         outsize = outsize.substring(0,index+3)+outsize.substring(outsize.length()-3,outsize.length());
        return outsize;
    }

}
