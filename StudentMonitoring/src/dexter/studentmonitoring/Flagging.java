package dexter.studentmonitoring;

public class Flagging {
	public static int myflag[]={1,1,1};
	public static void setFlag(int index)
	{
		
		myflag[index]=(myflag[index]+1)%2;
		if(myflag[index]==0)
			for(int i=0;i<3;i++)
			{
				if(i!=index)
				myflag[i]=1;
			}
	}

}
