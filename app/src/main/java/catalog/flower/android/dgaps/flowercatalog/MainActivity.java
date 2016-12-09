package catalog.flower.android.dgaps.flowercatalog;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView output;ProgressBar pb;
    List<MyTask> myTaskList;
    List<Flower> flowerList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        output = (TextView) findViewById(R.id.textView);
        myTaskList = new ArrayList<>();

        output.setMovementMethod( new ScrollingMovementMethod());

        pb = (ProgressBar) findViewById(R.id.progressBar);
        pb.setVisibility(View.INVISIBLE);


//        for (int i=0; i <=100;i++)
//        {
//            updateDisplay("Line: "+i+"\n");
//
//        }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);





        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.doTask) {

//            updateDisplay("Task Done ..... ");

            //myTask.execute("Param 1","Param 2","Param 3");
           if (isOnline()) {
               requestData("http://services.hanselandpetal.com/feeds/flowers.xml");
           }
            else
           {
               Toast.makeText(this, "Not Connected to Internet", Toast.LENGTH_SHORT).show();
           }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    private void requestData(String uri) {
        MyTask myTask = new MyTask();

       // myTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"Param 1","Param 2","Param 3");
        myTask.execute(uri);
    }

    public  void updateDisplay()
    {

        if (flowerList!=null)
        {

            for (Flower flower: flowerList) {

                output.append(flower.getName()+ "\n");
            }
        }

    }


    //  FOr NetworkConnectivity and Internet
    protected  Boolean isOnline()
    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo !=null && networkInfo.isConnectedOrConnecting())
        {
            return  true;
        }
        else
        {
            return  false;
        }


    }

    /// ************** New Class Async  Start **********************


     private  class  MyTask extends AsyncTask <String,String,String>
     {


         @Override
         protected void onPreExecute() {

          //   updateDisplay("Starting Task from onPre Execute Function\n");
             pb.setVisibility(View.VISIBLE);
         }

         @Override
         protected String doInBackground(String... strings) {

//
//              for (int i =0; i<strings.length;i++)
//             {
//
//
//                publishProgress("Working With "+ strings[i]);
//
//
//                 try {
//                     Thread.sleep(1200);
//                 }
//                 catch (Exception e)
//                 {
//
//                 }
//             }

            String content= HttpManager.getData(strings[0]);

        // updateDisplay("I am inside doIBackground");

             //return "Task Completed";

             return content;

         }

         @Override
         protected void onPostExecute(String s)

         {
             flowerList = FlowerXMLParser.parseFeed(s);
             updateDisplay();
         }


         @Override
         protected void onProgressUpdate(String... values) {


            // updateDisplay(values[0]);
             pb.setVisibility(View.INVISIBLE);
         }
     }



    //******************** New Class Async  Finish  *******
}
