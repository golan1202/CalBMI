package com.example.golan.calbmi;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.security.acl.Group;


public class MainActivity extends Activity {

    static int i = 0;
    static double sum = 0;
    static int males = 0;
    static int females = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView result = findViewById(R.id.bmi_output);
        final EditText weight_input = findViewById(R.id.weight_input);
        final EditText height_input = findViewById(R.id.height_input);
        final ImageView imageView = findViewById(R.id.image_res);
        final RadioButton radio_btn_male = findViewById(R.id.radio_btn_male);
        final RadioButton radio_btn_female = findViewById(R.id.radio_btn_female);
        final RadioGroup radio_group = (RadioGroup) findViewById(R.id.radio_group);
        Button clear_btn = findViewById(R.id.clear_btn);
        ImageButton finishBtn = findViewById(R.id.finish_btn);
        final GraphView graph = findViewById(R.id.graph);
        final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.infolayout);
        final TextView list = (TextView) findViewById(R.id.list);
        final TextView list_avg = (TextView) findViewById(R.id.list_avg);
        final TextView name = (TextView) findViewById(R.id.name);

        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(60);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(60);

        final LineGraphSeries<DataPoint> coo = new LineGraphSeries<>(new DataPoint[]{
                new DataPoint(0, 0),
                new DataPoint(60, 60),
        });
        final PointsGraphSeries<DataPoint> ser = new PointsGraphSeries<>(new DataPoint[]{
                new DataPoint(18.5, 18.5),
                new DataPoint(25, 25),
        });
        coo.setColor(getResources().getColor(R.color.light_blue));
        ser.setColor(getResources().getColor(R.color.black));
        ser.setShape(PointsGraphSeries.Shape.POINT);
        ser.setSize(15);

        graph.addSeries(ser);

        finishBtn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                if (name.getText().toString().matches("")) {
                    Toast.makeText(MainActivity.this, "Please enter your name", Toast.LENGTH_SHORT).show();
                    return false;
                }

                if(!name.getText().toString().matches("[a-zA-Zא-ת]+")){
                    Toast.makeText(MainActivity.this, "Please enter name with letters only", Toast.LENGTH_SHORT).show();
                    return false;
                }
                if(name.getText().toString().length()>25) {
                    Toast.makeText(MainActivity.this, "Please enter shorter name", Toast.LENGTH_SHORT).show();
                    return false;
                }
                if (!radio_btn_female.isChecked() & !radio_btn_male.isChecked()) {
                    Toast.makeText(MainActivity.this, "Please choose your gender", Toast.LENGTH_SHORT).show();
                    return false;
                }

                if ((height_input.getText().toString().matches("")) || (weight_input.getText().toString().matches(""))) {
                    Toast.makeText(MainActivity.this, "Please enter weight and height", Toast.LENGTH_SHORT).show();
                    return false;
                }

                String string = name.getText().toString();
                double n1 = Double.valueOf(weight_input.getText().toString());
                double n2 = Double.valueOf(height_input.getText().toString());


                if ((n1 > 300) || (n2 > 300) || (n1 < 20) || (n2 < 50)) {
                    Toast.makeText(MainActivity.this, "Choose logical values", Toast.LENGTH_SHORT).show();
                    return false;
                }
                double r = n1 / (n2 / 100 * n2 / 100);
                double diff1 = 18.5 * (n2 / 100 * n2 / 100) - n1;
                double diff2 = n1 - 25 * (n2 / 100 * n2 / 100);

                if (radio_btn_female.isChecked())
                    females++;
                else
                    males++;


                if (graph.getSeries() != null) {
                    graph.removeAllSeries();
                }
                if (i == 0) {
                    list.setVisibility(View.VISIBLE);
                }
                PointsGraphSeries<DataPoint> series = new PointsGraphSeries<>(new DataPoint[]{
                        new DataPoint(r, r)});
                series.setShape(PointsGraphSeries.Shape.POINT);
                series.setSize(10);
                series.setColor(getResources().getColor(R.color.red));

                graph.addSeries(coo);
                graph.addSeries(ser);
                graph.addSeries(series);
                graph.onDataChanged(true, true);


                LayoutInflater layoutInfralte = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = layoutInfralte.inflate(R.layout.child_page, null);
                TextView tv = (TextView) view.findViewById(R.id.bmi_output);
                ImageView iv = (ImageView) view.findViewById(R.id.image_res);
                GraphView g = (GraphView) view.findViewById(R.id.graph);
                g.addSeries(coo);
                g.addSeries(ser);
                g.addSeries(series);
                g.getViewport().setXAxisBoundsManual(true);
                g.getViewport().setMinX(0);
                g.getViewport().setMaxX(60);
                g.getViewport().setYAxisBoundsManual(true);
                g.getViewport().setMinY(0);
                g.getViewport().setMaxY(60);

                i++;
                sum += r;
                if (i > 1) {
                    list_avg.setVisibility(View.VISIBLE);
                    list_avg.setText(getString(R.string.average) + ": " + String.format("%.2f",sum /i)+"\n" +getString(R.string.no_males)+": "+males + " , "+ getString(R.string.no_females)+": "+females);
                }

                if (r < 18.5) {

                    result.setText(getString(R.string.hello) + " " + string + "\n" + getString(R.string.Your_BMI_is) + " " + String.format("%.2f", r) + "\n" + getString(R.string.You_should_eat_more_and_put_on_weight_at_least) + " " + String.format("%.2f", diff1) + " " + getString(R.string.more_kg));
                    imageView.setImageResource(R.drawable.oops);


                    tv.setText(i + ". " + string + "\n" + getString(R.string.Your_BMI_is) + " " + String.format("%.2f", r) + "\n" + getString(R.string.You_should_eat_more_and_put_on_weight_at_least) + " " + String.format("%.2f", diff1) + " " + getString(R.string.more_kg));
                    iv.setImageResource(R.drawable.oops);
                    linearLayout.addView(view);

                    return false;
                }
                if (r >= 18.5 & r <= 25) {
                    result.setText(getString(R.string.hello) + " " + string + "\n" + getString(R.string.Your_BMI_is) + " " + String.format("%.2f", r) + "\n" + getString(R.string.Keep_going_you_aregreat));
                    imageView.setImageResource(R.drawable.well_done);

                    tv.setText(i + ". " + string + "\n" + getString(R.string.Your_BMI_is) + " " + String.format("%.2f", r) + "\n" + getString(R.string.Keep_going_you_aregreat));
                    iv.setImageResource(R.drawable.well_done);
                    linearLayout.addView(view);

                    return false;
                }

                if (25 < r & r <= 30) {
                    result.setText(getString(R.string.hello) + " " + string + "\n" + getString(R.string.Your_BMI_is) + " " + String.format("%.2f", r) + "\n" + getString(R.string.You_should_try_to_do_more_workout_and_need_to_lose_on_weight_at_least) + " " + String.format("%.2f", diff2) + " " + getString(R.string.kg));
                    imageView.setImageResource(R.drawable.maybe_next_time);

                    tv.setText(i + ". " + string + "\n" + getString(R.string.Your_BMI_is) + " " + String.format("%.2f", r) + "\n" + getString(R.string.You_should_try_to_do_more_workout_and_need_to_lose_on_weight_at_least) + " " + String.format("%.2f", diff2) + " " + getString(R.string.kg));
                    iv.setImageResource(R.drawable.maybe_next_time);
                    linearLayout.addView(view);
                    return false;
                }

                if (r > 30) {
                    result.setText(getString(R.string.hello) + " " + string + "\n" + getString(R.string.Your_BMI_is) + " " + String.format("%.2f", r) + "\n" + getString(R.string.You_are_very_fat_and_need_to_lose_on_weight_at_least) + " " + String.format("%.2f", diff2) + " " + getString(R.string.kg));
                    imageView.setImageResource(R.drawable.fat);

                    tv.setText(i + ". " + string + "\n" + getString(R.string.Your_BMI_is) + " " + String.format("%.2f", r) + "\n" + getString(R.string.You_are_very_fat_and_need_to_lose_on_weight_at_least) + " " + String.format("%.2f", diff2) + " " + getString(R.string.kg));
                    iv.setImageResource(R.drawable.fat);
                    linearLayout.addView(view);
                    return false;
                }


                return false;
            }

        });

        clear_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                radio_group.clearCheck();
                weight_input.setText("");
                height_input.setText("");
                result.setText("");
                imageView.setImageBitmap(null);
                graph.removeAllSeries();
                name.setText("");

            }
        });

        clear_btn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                radio_group.clearCheck();
                weight_input.setText("");
                height_input.setText("");
                result.setText("");
                imageView.setImageBitmap(null);
                graph.removeAllSeries();
                name.setText("");

                linearLayout.removeAllViews();
                i = 0;
                sum = 0;
                males=0;
                females=0;
                list.setVisibility(View.INVISIBLE);
                list_avg.setVisibility(View.INVISIBLE);
                return false;
            }
        });


    }
}
