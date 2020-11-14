package com.audioplayer.androidaudiostreaming;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.audioplayer.androidaudiostreaming.model.AxStreamModel;
import com.audioplayer.androidaudiostreaming.model.ExpressionKeyFrame;
import com.audioplayer.androidaudiostreaming.model.GetNextScheduledClassesRes;
import com.audioplayer.androidaudiostreaming.model.MediaMetaData;
import com.audioplayer.androidaudiostreaming.model.ExpressionModel;
import com.google.gson.Gson;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class Hello extends AppCompatActivity {

    private static final String TAG = "Hello";
    private ArrayList<AxStreamModel> axStreamModelArrayList;
    private ArrayList<MediaMetaData> seqArrayList;
    private ImageView imageView;
    private View mView, layoutAnim;
    private ExpressionKeyFrame expressionKeyFrame;
    private ArrayList<ExpressionKeyFrame> expressionKeyFrameArrayList;
    private ArrayList<String> speechArray;
    // To add filtered Speech array that is with ax_stream and not otherise.
    private ArrayList<String> filteredBlockArray;
    private ArrayList<String> speech;
    private LinkedList<ExpressionKeyFrame> expressionKeyLinkedList;
    private long currentExpressionIndex = 0;

    private Button button;
    MediaMetaData seq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.play);
        seqArrayList = new ArrayList<>();
        expressionKeyFrameArrayList = new ArrayList<>();
        filteredBlockArray = new ArrayList<>();

        filteredBlockArray.add("<expression>{\\\"tx\\\":{\\\"type\\\":11,\\\"size\\\":0,\\\"loop\\\":1,\\\"seqCount\\\":1,\\\"seq\\\":[{\\\"data\\\":\\\"\\\",\\\"length\\\":0,\\\"id\\\":0,\\\"time\\\":-1,\\\"data_dsp\\\":\\\"\\\",\\\"dsp_size\\\":0}]},\\\"ax\\\":{\\\"type\\\":0,\\\"size\\\":0,\\\"loop\\\":0,\\\"loop1\\\":0,\\\"seqCount\\\":0,\\\"seq\\\":[]},\\\"ax_stream\\\":{\\\"type\\\":15,\\\"size\\\":0,\\\"loop\\\":1,\\\"seqCount\\\":1,\\\"seq\\\":[{\\\"id\\\":0,\\\"title\\\":\\\"INTRO\\\",\\\"trackNumber\\\":0,\\\"totalTrackCount\\\":0,\\\"offsetStart\\\":\\\"-19798000\\\",\\\"offsetEnd\\\":\\\"-19566000\\\",\\\"site\\\":\\\"https://miko2.s3.ap-south-1.amazonaws.com/test/KidNuz_08_12_20+(1).mp3\\\"}]},\\\"mx\\\":{\\\"type\\\":0,\\\"size\\\":0,\\\"motion_type\\\":0,\\\"loop\\\":0,\\\"seqCount\\\":0,\\\"seq\\\":[]},\\\"mx2\\\":{\\\"kp\\\":0,\\\"ki\\\":0,\\\"kd\\\":0,\\\"target_angle\\\":0,\\\"zonea\\\":0,\\\"zoneb\\\":0,\\\"positionScaleA\\\":0,\\\"positionScaleB\\\":0,\\\"positionScaleC\\\":0,\\\"velocityScaleStop\\\":0,\\\"velocityScaleMove\\\":0,\\\"onewheel\\\":0,\\\"falst\\\":0,\\\"mston\\\":0,\\\"mston_flag\\\":0,\\\"type\\\":0,\\\"size\\\":0,\\\"motion_type\\\":0,\\\"loop\\\":0,\\\"steer\\\":0,\\\"seqCount\\\":0,\\\"seq\\\":[]},\\\"mx3\\\":{\\\"type\\\":0,\\\"size\\\":0,\\\"motion_type\\\":0,\\\"loop\\\":0,\\\"seqCount\\\":0,\\\"seq\\\":[]},\\\"ix1\\\":{\\\"type\\\":11,\\\"size\\\":0,\\\"imagetype\\\":0,\\\"loop\\\":0,\\\"seqCount\\\":1,\\\"seq\\\":[{\\\"frame\\\":\\\"blink.json\\\",\\\"rate\\\":0,\\\"id\\\":0,\\\"loop\\\":0}]},\\\"rx\\\":{\\\"type\\\":5,\\\"size\\\":0,\\\"loop\\\":0,\\\"seqCount\\\":1,\\\"seq\\\":[{\\\"pattern\\\":0,\\\"color\\\":8,\\\"time\\\":0,\\\"rate\\\":0,\\\"id\\\":0}]},\\\"dx\\\":{\\\"type\\\":0,\\\"size\\\":0,\\\"loop\\\":0,\\\"seqCount\\\":0,\\\"seq\\\":[]},\\\"atx\\\":{\\\"type\\\":0,\\\"size\\\":0,\\\"loop\\\":0,\\\"seqCount\\\":0,\\\"seq\\\":[]},\\\"msg\\\":{\\\"seqCount\\\":0,\\\"seq\\\":[]},\\\"id\\\":0,\\\"vel2_shape\\\":false,\\\"fallstatus\\\":0,\\\"chargerStatus\\\":false}</expression>");

        filteredBlockArray.add("<expression>{\\\"tx\\\":{\\\"type\\\":0,\\\"size\\\":0,\\\"loop\\\":0,\\\"seqCount\\\":0,\\\"seq\\\":[]},\\\"ax\\\":{\\\"type\\\":0,\\\"size\\\":0,\\\"loop\\\":0,\\\"loop1\\\":0,\\\"seqCount\\\":0,\\\"seq\\\":[]},\\\"ax_stream\\\":{\\\"type\\\":15,\\\"size\\\":0,\\\"loop\\\":1,\\\"seqCount\\\":1,\\\"seq\\\":[{\\\"id\\\":0,\\\"title\\\":\\\"NEWS-1\\\",\\\"trackNumber\\\":0,\\\"totalTrackCount\\\":0,\\\"offsetStart\\\":\\\"-19793000\\\",\\\"offsetEnd\\\":\\\"-19746000\\\",\\\"site\\\":\\\"https://miko2.s3.ap-south-1.amazonaws.com/test/KidNuz_08_12_20+(1).mp3\\\"}]},\\\"mx\\\":{\\\"type\\\":4,\\\"size\\\":0,\\\"motion_type\\\":4,\\\"loop\\\":3,\\\"seqCount\\\":4,\\\"seq\\\":[{\\\"linear\\\":0,\\\"angular\\\":4,\\\"time\\\":1,\\\"type\\\":4,\\\"id\\\":0},{\\\"linear\\\":0,\\\"angular\\\":-5,\\\"time\\\":1,\\\"type\\\":4,\\\"id\\\":1},{\\\"linear\\\":0,\\\"angular\\\":5,\\\"time\\\":1,\\\"type\\\":4,\\\"id\\\":2},{\\\"linear\\\":0,\\\"angular\\\":-4,\\\"time\\\":1,\\\"type\\\":4,\\\"id\\\":3}]},\\\"mx2\\\":{\\\"kp\\\":0,\\\"ki\\\":0,\\\"kd\\\":0,\\\"target_angle\\\":0,\\\"zonea\\\":0,\\\"zoneb\\\":0,\\\"positionScaleA\\\":0,\\\"positionScaleB\\\":0,\\\"positionScaleC\\\":0,\\\"velocityScaleStop\\\":0,\\\"velocityScaleMove\\\":0,\\\"onewheel\\\":0,\\\"falst\\\":0,\\\"mston\\\":0,\\\"mston_flag\\\":0,\\\"type\\\":0,\\\"size\\\":0,\\\"motion_type\\\":0,\\\"loop\\\":0,\\\"steer\\\":0,\\\"seqCount\\\":0,\\\"seq\\\":[]},\\\"mx3\\\":{\\\"type\\\":0,\\\"size\\\":0,\\\"motion_type\\\":0,\\\"loop\\\":0,\\\"seqCount\\\":0,\\\"seq\\\":[]},\\\"ix1\\\":{\\\"type\\\":3,\\\"size\\\":0,\\\"imagetype\\\":1,\\\"loop\\\":0,\\\"seqCount\\\":1,\\\"seq\\\":[{\\\"frame\\\":\\\"b-89.png\\\",\\\"rate\\\":0,\\\"id\\\":0,\\\"loop\\\":0}]},\\\"rx\\\":{\\\"type\\\":5,\\\"size\\\":0,\\\"loop\\\":1,\\\"seqCount\\\":1,\\\"seq\\\":[{\\\"pattern\\\":5,\\\"color\\\":8,\\\"time\\\":0,\\\"rate\\\":17,\\\"id\\\":0}]},\\\"dx\\\":{\\\"type\\\":13,\\\"size\\\":0,\\\"loop\\\":1,\\\"seqCount\\\":1,\\\"seq\\\":[{\\\"length\\\":0,\\\"id\\\":0,\\\"time\\\":8000}]},\\\"atx\\\":{\\\"type\\\":0,\\\"size\\\":0,\\\"loop\\\":0,\\\"seqCount\\\":0,\\\"seq\\\":[]},\\\"msg\\\":{\\\"seqCount\\\":0,\\\"seq\\\":[]},\\\"id\\\":0,\\\"vel2_shape\\\":false,\\\"fallstatus\\\":0,\\\"chargerStatus\\\":false}</expression>");

        filteredBlockArray.add("<expression>{\\\"tx\\\":{\\\"type\\\":0,\\\"size\\\":0,\\\"loop\\\":0,\\\"seqCount\\\":0,\\\"seq\\\":[]},\\\"ax\\\":{\\\"type\\\":0,\\\"size\\\":0,\\\"loop\\\":0,\\\"loop1\\\":0,\\\"seqCount\\\":0,\\\"seq\\\":[]},\\\"ax_stream\\\":{\\\"type\\\":15,\\\"size\\\":0,\\\"loop\\\":1,\\\"seqCount\\\":1,\\\"seq\\\":[{\\\"id\\\":0,\\\"title\\\":\\\"NEWS-2\\\",\\\"trackNumber\\\":0,\\\"totalTrackCount\\\":0,\\\"offsetStart\\\":\\\"-19746000\\\",\\\"offsetEnd\\\":\\\"-19689000\\\",\\\"site\\\":\\\"https://miko2.s3.ap-south-1.amazonaws.com/test/KidNuz_08_12_20+(1).mp3\\\"}]},\\\"mx\\\":{\\\"type\\\":4,\\\"size\\\":0,\\\"motion_type\\\":4,\\\"loop\\\":3,\\\"seqCount\\\":6,\\\"seq\\\":[{\\\"linear\\\":-850,\\\"angular\\\":0,\\\"time\\\":6,\\\"type\\\":1,\\\"id\\\":0},{\\\"linear\\\":850,\\\"angular\\\":0,\\\"time\\\":3,\\\"type\\\":1,\\\"id\\\":1},{\\\"linear\\\":-850,\\\"angular\\\":0,\\\"time\\\":3,\\\"type\\\":1,\\\"id\\\":2},{\\\"linear\\\":850,\\\"angular\\\":0,\\\"time\\\":3,\\\"type\\\":1,\\\"id\\\":3},{\\\"linear\\\":-850,\\\"angular\\\":0,\\\"time\\\":3,\\\"type\\\":1,\\\"id\\\":4},{\\\"linear\\\":850,\\\"angular\\\":0,\\\"time\\\":3,\\\"type\\\":1,\\\"id\\\":5}]},\\\"mx2\\\":{\\\"kp\\\":0,\\\"ki\\\":0,\\\"kd\\\":0,\\\"target_angle\\\":0,\\\"zonea\\\":0,\\\"zoneb\\\":0,\\\"positionScaleA\\\":0,\\\"positionScaleB\\\":0,\\\"positionScaleC\\\":0,\\\"velocityScaleStop\\\":0,\\\"velocityScaleMove\\\":0,\\\"onewheel\\\":0,\\\"falst\\\":0,\\\"mston\\\":0,\\\"mston_flag\\\":0,\\\"type\\\":0,\\\"size\\\":0,\\\"motion_type\\\":0,\\\"loop\\\":0,\\\"steer\\\":0,\\\"seqCount\\\":0,\\\"seq\\\":[]},\\\"mx3\\\":{\\\"type\\\":0,\\\"size\\\":0,\\\"motion_type\\\":0,\\\"loop\\\":0,\\\"seqCount\\\":0,\\\"seq\\\":[]},\\\"ix1\\\":{\\\"type\\\":3,\\\"size\\\":0,\\\"imagetype\\\":1,\\\"loop\\\":0,\\\"seqCount\\\":1,\\\"seq\\\":[{\\\"frame\\\":\\\"b-90.png\\\",\\\"rate\\\":2,\\\"id\\\":0,\\\"loop\\\":0}]},\\\"rx\\\":{\\\"type\\\":5,\\\"size\\\":0,\\\"loop\\\":0,\\\"seqCount\\\":1,\\\"seq\\\":[{\\\"pattern\\\":5,\\\"color\\\":7,\\\"time\\\":0,\\\"rate\\\":17,\\\"id\\\":0}]},\\\"dx\\\":{\\\"type\\\":13,\\\"size\\\":0,\\\"loop\\\":1,\\\"seqCount\\\":1,\\\"seq\\\":[{\\\"length\\\":0,\\\"id\\\":0,\\\"time\\\":7000}]},\\\"atx\\\":{\\\"type\\\":0,\\\"size\\\":0,\\\"loop\\\":0,\\\"seqCount\\\":0,\\\"seq\\\":[]},\\\"msg\\\":{\\\"seqCount\\\":0,\\\"seq\\\":[]},\\\"id\\\":-928,\\\"vel2_shape\\\":false,\\\"fallstatus\\\":0,\\\"chargerStatus\\\":false}</expression>");

        filteredBlockArray.add("<expression>{\\\"tx\\\":{\\\"type\\\":0,\\\"size\\\":0,\\\"loop\\\":0,\\\"seqCount\\\":0,\\\"seq\\\":[]},\\\"ax\\\":{\\\"type\\\":0,\\\"size\\\":0,\\\"loop\\\":0,\\\"loop1\\\":0,\\\"seqCount\\\":0,\\\"seq\\\":[]},\\\"ax_stream\\\":{\\\"type\\\":15,\\\"size\\\":0,\\\"loop\\\":1,\\\"seqCount\\\":1,\\\"seq\\\":[{\\\"id\\\":0,\\\"title\\\":\\\"NEWS-3\\\",\\\"trackNumber\\\":0,\\\"totalTrackCount\\\":0,\\\"offsetStart\\\":\\\"-19689000\\\",\\\"offsetEnd\\\":\\\"-19659000\\\",\\\"site\\\":\\\"https://miko2.s3.ap-south-1.amazonaws.com/test/KidNuz_08_12_20+(1).mp3\\\"}]},\\\"mx\\\":{\\\"type\\\":4,\\\"size\\\":0,\\\"motion_type\\\":4,\\\"loop\\\":3,\\\"seqCount\\\":4,\\\"seq\\\":[{\\\"linear\\\":0,\\\"angular\\\":4,\\\"time\\\":1,\\\"type\\\":4,\\\"id\\\":0},{\\\"linear\\\":0,\\\"angular\\\":-5,\\\"time\\\":1,\\\"type\\\":4,\\\"id\\\":1},{\\\"linear\\\":0,\\\"angular\\\":5,\\\"time\\\":1,\\\"type\\\":4,\\\"id\\\":2},{\\\"linear\\\":0,\\\"angular\\\":-4,\\\"time\\\":1,\\\"type\\\":4,\\\"id\\\":3}]},\\\"mx2\\\":{\\\"kp\\\":0,\\\"ki\\\":0,\\\"kd\\\":0,\\\"target_angle\\\":0,\\\"zonea\\\":0,\\\"zoneb\\\":0,\\\"positionScaleA\\\":0,\\\"positionScaleB\\\":0,\\\"positionScaleC\\\":0,\\\"velocityScaleStop\\\":0,\\\"velocityScaleMove\\\":0,\\\"onewheel\\\":0,\\\"falst\\\":0,\\\"mston\\\":0,\\\"mston_flag\\\":0,\\\"type\\\":0,\\\"size\\\":0,\\\"motion_type\\\":0,\\\"loop\\\":0,\\\"steer\\\":0,\\\"seqCount\\\":0,\\\"seq\\\":[]},\\\"mx3\\\":{\\\"type\\\":0,\\\"size\\\":0,\\\"motion_type\\\":0,\\\"loop\\\":0,\\\"seqCount\\\":0,\\\"seq\\\":[]},\\\"ix1\\\":{\\\"type\\\":3,\\\"size\\\":0,\\\"imagetype\\\":1,\\\"loop\\\":0,\\\"seqCount\\\":1,\\\"seq\\\":[{\\\"frame\\\":\\\"b-91.png\\\",\\\"rate\\\":0,\\\"id\\\":0,\\\"loop\\\":0}]},\\\"rx\\\":{\\\"type\\\":5,\\\"size\\\":0,\\\"loop\\\":1,\\\"seqCount\\\":1,\\\"seq\\\":[{\\\"pattern\\\":5,\\\"color\\\":8,\\\"time\\\":0,\\\"rate\\\":17,\\\"id\\\":0}]},\\\"dx\\\":{\\\"type\\\":13,\\\"size\\\":0,\\\"loop\\\":1,\\\"seqCount\\\":1,\\\"seq\\\":[{\\\"length\\\":0,\\\"id\\\":0,\\\"time\\\":8000}]},\\\"atx\\\":{\\\"type\\\":0,\\\"size\\\":0,\\\"loop\\\":0,\\\"seqCount\\\":0,\\\"seq\\\":[]},\\\"msg\\\":{\\\"seqCount\\\":0,\\\"seq\\\":[]},\\\"id\\\":0,\\\"vel2_shape\\\":false,\\\"fallstatus\\\":0,\\\"chargerStatus\\\":false}</expression>");

        filteredBlockArray.add("<expression>{\\\"tx\\\":{\\\"type\\\":0,\\\"size\\\":0,\\\"loop\\\":0,\\\"seqCount\\\":0,\\\"seq\\\":[]},\\\"ax\\\":{\\\"type\\\":0,\\\"size\\\":0,\\\"loop\\\":0,\\\"loop1\\\":0,\\\"seqCount\\\":0,\\\"seq\\\":[]},\\\"ax_stream\\\":{\\\"type\\\":15,\\\"size\\\":0,\\\"loop\\\":1,\\\"seqCount\\\":1,\\\"seq\\\":[{\\\"id\\\":0,\\\"title\\\":\\\"NEWS-4\\\",\\\"trackNumber\\\":0,\\\"totalTrackCount\\\":0,\\\"offsetStart\\\":\\\"-19659000\\\",\\\"offsetEnd\\\":\\\"-19611000\\\",\\\"site\\\":\\\"https://miko2.s3.ap-south-1.amazonaws.com/test/KidNuz_08_12_20+(1).mp3\\\"}]},\\\"mx\\\":{\\\"type\\\":4,\\\"size\\\":0,\\\"motion_type\\\":4,\\\"loop\\\":3,\\\"seqCount\\\":6,\\\"seq\\\":[{\\\"linear\\\":-850,\\\"angular\\\":0,\\\"time\\\":6,\\\"type\\\":1,\\\"id\\\":0},{\\\"linear\\\":850,\\\"angular\\\":0,\\\"time\\\":3,\\\"type\\\":1,\\\"id\\\":1},{\\\"linear\\\":-850,\\\"angular\\\":0,\\\"time\\\":3,\\\"type\\\":1,\\\"id\\\":2},{\\\"linear\\\":850,\\\"angular\\\":0,\\\"time\\\":3,\\\"type\\\":1,\\\"id\\\":3},{\\\"linear\\\":-850,\\\"angular\\\":0,\\\"time\\\":3,\\\"type\\\":1,\\\"id\\\":4},{\\\"linear\\\":850,\\\"angular\\\":0,\\\"time\\\":3,\\\"type\\\":1,\\\"id\\\":5}]},\\\"mx2\\\":{\\\"kp\\\":0,\\\"ki\\\":0,\\\"kd\\\":0,\\\"target_angle\\\":0,\\\"zonea\\\":0,\\\"zoneb\\\":0,\\\"positionScaleA\\\":0,\\\"positionScaleB\\\":0,\\\"positionScaleC\\\":0,\\\"velocityScaleStop\\\":0,\\\"velocityScaleMove\\\":0,\\\"onewheel\\\":0,\\\"falst\\\":0,\\\"mston\\\":0,\\\"mston_flag\\\":0,\\\"type\\\":0,\\\"size\\\":0,\\\"motion_type\\\":0,\\\"loop\\\":0,\\\"steer\\\":0,\\\"seqCount\\\":0,\\\"seq\\\":[]},\\\"mx3\\\":{\\\"type\\\":0,\\\"size\\\":0,\\\"motion_type\\\":0,\\\"loop\\\":0,\\\"seqCount\\\":0,\\\"seq\\\":[]},\\\"ix1\\\":{\\\"type\\\":3,\\\"size\\\":0,\\\"imagetype\\\":1,\\\"loop\\\":0,\\\"seqCount\\\":1,\\\"seq\\\":[{\\\"frame\\\":\\\"b-97.png\\\",\\\"rate\\\":2,\\\"id\\\":0,\\\"loop\\\":0}]},\\\"rx\\\":{\\\"type\\\":5,\\\"size\\\":0,\\\"loop\\\":0,\\\"seqCount\\\":1,\\\"seq\\\":[{\\\"pattern\\\":5,\\\"color\\\":7,\\\"time\\\":0,\\\"rate\\\":17,\\\"id\\\":0}]},\\\"dx\\\":{\\\"type\\\":13,\\\"size\\\":0,\\\"loop\\\":1,\\\"seqCount\\\":1,\\\"seq\\\":[{\\\"length\\\":0,\\\"id\\\":0,\\\"time\\\":7000}]},\\\"atx\\\":{\\\"type\\\":0,\\\"size\\\":0,\\\"loop\\\":0,\\\"seqCount\\\":0,\\\"seq\\\":[]},\\\"msg\\\":{\\\"seqCount\\\":0,\\\"seq\\\":[]},\\\"id\\\":-928,\\\"vel2_shape\\\":false,\\\"fallstatus\\\":0,\\\"chargerStatus\\\":false}</expression>");

        filteredBlockArray.add("<expression>{\\\"tx\\\":{\\\"type\\\":0,\\\"size\\\":0,\\\"loop\\\":0,\\\"seqCount\\\":0,\\\"seq\\\":[]},\\\"ax\\\":{\\\"type\\\":0,\\\"size\\\":0,\\\"loop\\\":0,\\\"loop1\\\":0,\\\"seqCount\\\":0,\\\"seq\\\":[]},\\\"ax_stream\\\":{\\\"type\\\":15,\\\"size\\\":0,\\\"loop\\\":1,\\\"seqCount\\\":1,\\\"seq\\\":[{\\\"id\\\":0,\\\"title\\\":\\\"NEWS-5\\\",\\\"trackNumber\\\":0,\\\"totalTrackCount\\\":0,\\\"offsetStart\\\":\\\"-19611000\\\",\\\"offsetEnd\\\":\\\"-19590000\\\",\\\"site\\\":\\\"https://miko2.s3.ap-south-1.amazonaws.com/test/KidNuz_08_12_20+(1).mp3\\\"}]},\\\"mx\\\":{\\\"type\\\":4,\\\"size\\\":0,\\\"motion_type\\\":4,\\\"loop\\\":3,\\\"seqCount\\\":4,\\\"seq\\\":[{\\\"linear\\\":0,\\\"angular\\\":4,\\\"time\\\":1,\\\"type\\\":4,\\\"id\\\":0},{\\\"linear\\\":0,\\\"angular\\\":-5,\\\"time\\\":1,\\\"type\\\":4,\\\"id\\\":1},{\\\"linear\\\":0,\\\"angular\\\":5,\\\"time\\\":1,\\\"type\\\":4,\\\"id\\\":2},{\\\"linear\\\":0,\\\"angular\\\":-4,\\\"time\\\":1,\\\"type\\\":4,\\\"id\\\":3}]},\\\"mx2\\\":{\\\"kp\\\":0,\\\"ki\\\":0,\\\"kd\\\":0,\\\"target_angle\\\":0,\\\"zonea\\\":0,\\\"zoneb\\\":0,\\\"positionScaleA\\\":0,\\\"positionScaleB\\\":0,\\\"positionScaleC\\\":0,\\\"velocityScaleStop\\\":0,\\\"velocityScaleMove\\\":0,\\\"onewheel\\\":0,\\\"falst\\\":0,\\\"mston\\\":0,\\\"mston_flag\\\":0,\\\"type\\\":0,\\\"size\\\":0,\\\"motion_type\\\":0,\\\"loop\\\":0,\\\"steer\\\":0,\\\"seqCount\\\":0,\\\"seq\\\":[]},\\\"mx3\\\":{\\\"type\\\":0,\\\"size\\\":0,\\\"motion_type\\\":0,\\\"loop\\\":0,\\\"seqCount\\\":0,\\\"seq\\\":[]},\\\"ix1\\\":{\\\"type\\\":3,\\\"size\\\":0,\\\"imagetype\\\":1,\\\"loop\\\":0,\\\"seqCount\\\":1,\\\"seq\\\":[{\\\"frame\\\":\\\"b-98.png\\\",\\\"rate\\\":0,\\\"id\\\":0,\\\"loop\\\":0}]},\\\"rx\\\":{\\\"type\\\":5,\\\"size\\\":0,\\\"loop\\\":1,\\\"seqCount\\\":1,\\\"seq\\\":[{\\\"pattern\\\":5,\\\"color\\\":8,\\\"time\\\":0,\\\"rate\\\":17,\\\"id\\\":0}]},\\\"dx\\\":{\\\"type\\\":13,\\\"size\\\":0,\\\"loop\\\":1,\\\"seqCount\\\":1,\\\"seq\\\":[{\\\"length\\\":0,\\\"id\\\":0,\\\"time\\\":8000}]},\\\"atx\\\":{\\\"type\\\":0,\\\"size\\\":0,\\\"loop\\\":0,\\\"seqCount\\\":0,\\\"seq\\\":[]},\\\"msg\\\":{\\\"seqCount\\\":0,\\\"seq\\\":[]},\\\"id\\\":0,\\\"vel2_shape\\\":false,\\\"fallstatus\\\":0,\\\"chargerStatus\\\":false}</expression>");

        filteredBlockArray.add("<expression>{\\\"tx\\\":{\\\"type\\\":0,\\\"size\\\":0,\\\"loop\\\":0,\\\"seqCount\\\":0,\\\"seq\\\":[]},\\\"ax\\\":{\\\"type\\\":0,\\\"size\\\":0,\\\"loop\\\":0,\\\"loop1\\\":0,\\\"seqCount\\\":0,\\\"seq\\\":[]},\\\"ax_stream\\\":{\\\"type\\\":15,\\\"size\\\":0,\\\"loop\\\":1,\\\"seqCount\\\":1,\\\"seq\\\":[{\\\"id\\\":0,\\\"title\\\":\\\"NEWS-6\\\",\\\"trackNumber\\\":0,\\\"totalTrackCount\\\":0,\\\"offsetStart\\\":\\\"-19590000\\\",\\\"offsetEnd\\\":\\\"-19566000\\\",\\\"site\\\":\\\"https://miko2.s3.ap-south-1.amazonaws.com/test/KidNuz_08_12_20+(1).mp3\\\"}]},\\\"mx\\\":{\\\"type\\\":4,\\\"size\\\":0,\\\"motion_type\\\":4,\\\"loop\\\":3,\\\"seqCount\\\":6,\\\"seq\\\":[{\\\"linear\\\":-850,\\\"angular\\\":0,\\\"time\\\":6,\\\"type\\\":1,\\\"id\\\":0},{\\\"linear\\\":850,\\\"angular\\\":0,\\\"time\\\":3,\\\"type\\\":1,\\\"id\\\":1},{\\\"linear\\\":-850,\\\"angular\\\":0,\\\"time\\\":3,\\\"type\\\":1,\\\"id\\\":2},{\\\"linear\\\":850,\\\"angular\\\":0,\\\"time\\\":3,\\\"type\\\":1,\\\"id\\\":3},{\\\"linear\\\":-850,\\\"angular\\\":0,\\\"time\\\":3,\\\"type\\\":1,\\\"id\\\":4},{\\\"linear\\\":850,\\\"angular\\\":0,\\\"time\\\":3,\\\"type\\\":1,\\\"id\\\":5}]},\\\"mx2\\\":{\\\"kp\\\":0,\\\"ki\\\":0,\\\"kd\\\":0,\\\"target_angle\\\":0,\\\"zonea\\\":0,\\\"zoneb\\\":0,\\\"positionScaleA\\\":0,\\\"positionScaleB\\\":0,\\\"positionScaleC\\\":0,\\\"velocityScaleStop\\\":0,\\\"velocityScaleMove\\\":0,\\\"onewheel\\\":0,\\\"falst\\\":0,\\\"mston\\\":0,\\\"mston_flag\\\":0,\\\"type\\\":0,\\\"size\\\":0,\\\"motion_type\\\":0,\\\"loop\\\":0,\\\"steer\\\":0,\\\"seqCount\\\":0,\\\"seq\\\":[]},\\\"mx3\\\":{\\\"type\\\":0,\\\"size\\\":0,\\\"motion_type\\\":0,\\\"loop\\\":0,\\\"seqCount\\\":0,\\\"seq\\\":[]},\\\"ix1\\\":{\\\"type\\\":3,\\\"size\\\":0,\\\"imagetype\\\":1,\\\"loop\\\":0,\\\"seqCount\\\":1,\\\"seq\\\":[{\\\"frame\\\":\\\"b-99.png\\\",\\\"rate\\\":2,\\\"id\\\":0,\\\"loop\\\":0}]},\\\"rx\\\":{\\\"type\\\":5,\\\"size\\\":0,\\\"loop\\\":0,\\\"seqCount\\\":1,\\\"seq\\\":[{\\\"pattern\\\":5,\\\"color\\\":7,\\\"time\\\":0,\\\"rate\\\":15,\\\"id\\\":0}]},\\\"dx\\\":{\\\"type\\\":13,\\\"size\\\":0,\\\"loop\\\":1,\\\"seqCount\\\":1,\\\"seq\\\":[{\\\"length\\\":0,\\\"id\\\":0,\\\"time\\\":7000}]},\\\"atx\\\":{\\\"type\\\":0,\\\"size\\\":0,\\\"loop\\\":0,\\\"seqCount\\\":0,\\\"seq\\\":[]},\\\"msg\\\":{\\\"seqCount\\\":0,\\\"seq\\\":[]},\\\"id\\\":-928,\\\"vel2_shape\\\":false,\\\"fallstatus\\\":0,\\\"chargerStatus\\\":false}</expression>");
        speech = new ArrayList<>();


        speech = new ArrayList<>();
        speech.add("<block> \n<expression>{\"tx\":{\"type\":11,\"size\":0,\"loop\":1,\"seqCount\":1,\"seq\":[{\"data\":\"\",\"length\":0,\"id\":0,\"time\":-1,\"data_dsp\":\"\",\"dsp_size\":0}]},\"ax\":{\"type\":0,\"size\":0,\"loop\":0,\"loop1\":0,\"seqCount\":0,\"seq\":[]},\"ax_stream\":{\"type\":15,\"size\":0,\"loop\":1,\"seqCount\":1,\"seq\":[{\"id\":0,\"title\":\"INTRO\",\"trackNumber\":0,\"totalTrackCount\":0,\"offsetStart\":\"-19798000\",\"offsetEnd\":\"-19566000\",\"site\":\"https://miko2.s3.ap-south-1.amazonaws.com/test/KidNuz_08_12_20+(1).mp3\"}]},\"mx\":{\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"mx2\":{\"kp\":0,\"ki\":0,\"kd\":0,\"target_angle\":0,\"zonea\":0,\"zoneb\":0,\"positionScaleA\":0,\"positionScaleB\":0,\"positionScaleC\":0,\"velocityScaleStop\":0,\"velocityScaleMove\":0,\"onewheel\":0,\"falst\":0,\"mston\":0,\"mston_flag\":0,\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"steer\":0,\"seqCount\":0,\"seq\":[]},\"mx3\":{\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"ix1\":{\"type\":11,\"size\":0,\"imagetype\":0,\"loop\":0,\"seqCount\":1,\"seq\":[{\"frame\":\"blink.json\",\"rate\":0,\"id\":0,\"loop\":0}]},\"rx\":{\"type\":5,\"size\":0,\"loop\":0,\"seqCount\":1,\"seq\":[{\"pattern\":0,\"color\":8,\"time\":0,\"rate\":0,\"id\":0}]},\"dx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"atx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"msg\":{\"seqCount\":0,\"seq\":[]},\"id\":0,\"vel2_shape\":false,\"fallstatus\":0,\"chargerStatus\":false}</expression>\n<expression>{\"tx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"ax\":{\"type\":0,\"size\":0,\"loop\":0,\"loop1\":0,\"seqCount\":0,\"seq\":[]},\"ax_stream\":{\"type\":15,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"mx\":{\"type\":4,\"size\":0,\"motion_type\":4,\"loop\":3,\"seqCount\":4,\"seq\":[{\"linear\":0,\"angular\":4,\"time\":1,\"type\":4,\"id\":0},{\"linear\":0,\"angular\":-5,\"time\":1,\"type\":4,\"id\":1},{\"linear\":0,\"angular\":5,\"time\":1,\"type\":4,\"id\":2},{\"linear\":0,\"angular\":-4,\"time\":1,\"type\":4,\"id\":3}]},\"mx2\":{\"kp\":0,\"ki\":0,\"kd\":0,\"target_angle\":0,\"zonea\":0,\"zoneb\":0,\"positionScaleA\":0,\"positionScaleB\":0,\"positionScaleC\":0,\"velocityScaleStop\":0,\"velocityScaleMove\":0,\"onewheel\":0,\"falst\":0,\"mston\":0,\"mston_flag\":0,\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"steer\":0,\"seqCount\":0,\"seq\":[]},\"mx3\":{\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"ix1\":{\"type\":3,\"size\":0,\"imagetype\":1,\"loop\":0,\"seqCount\":1,\"seq\":[{\"frame\":\"b-89.png\",\"rate\":0,\"id\":0,\"loop\":0}]},\"rx\":{\"type\":5,\"size\":0,\"loop\":1,\"seqCount\":1,\"seq\":[{\"pattern\":5,\"color\":8,\"time\":0,\"rate\":17,\"id\":0}]},\"dx\":{\"type\":13,\"size\":0,\"loop\":1,\"seqCount\":1,\"seq\":[{\"length\":0,\"id\":0,\"time\":8000}]},\"atx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"msg\":{\"seqCount\":0,\"seq\":[]},\"id\":0,\"vel2_shape\":false,\"fallstatus\":0,\"chargerStatus\":false}</expression>\n<expression>{\"tx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"ax\":{\"type\":0,\"size\":0,\"loop\":0,\"loop1\":0,\"seqCount\":0,\"seq\":[]},\"ax_stream\":{\"type\":15,\"size\":0,\"loop\":1,\"seqCount\":1,\"seq\":[{\"id\":0,\"title\":\"NEWS-1\",\"trackNumber\":0,\"totalTrackCount\":0,\"offsetStart\":\"-19793000\",\"offsetEnd\":\"-19746000\",\"site\":\"https://miko2.s3.ap-south-1.amazonaws.com/test/KidNuz_08_12_20+(1).mp3\"}]},\"mx\":{\"type\":4,\"size\":0,\"motion_type\":4,\"loop\":3,\"seqCount\":4,\"seq\":[{\"linear\":0,\"angular\":4,\"time\":1,\"type\":4,\"id\":0},{\"linear\":0,\"angular\":-5,\"time\":1,\"type\":4,\"id\":1},{\"linear\":0,\"angular\":5,\"time\":1,\"type\":4,\"id\":2},{\"linear\":0,\"angular\":-4,\"time\":1,\"type\":4,\"id\":3}]},\"mx2\":{\"kp\":0,\"ki\":0,\"kd\":0,\"target_angle\":0,\"zonea\":0,\"zoneb\":0,\"positionScaleA\":0,\"positionScaleB\":0,\"positionScaleC\":0,\"velocityScaleStop\":0,\"velocityScaleMove\":0,\"onewheel\":0,\"falst\":0,\"mston\":0,\"mston_flag\":0,\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"steer\":0,\"seqCount\":0,\"seq\":[]},\"mx3\":{\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"ix1\":{\"type\":3,\"size\":0,\"imagetype\":1,\"loop\":0,\"seqCount\":1,\"seq\":[{\"frame\":\"b-89.png\",\"rate\":0,\"id\":0,\"loop\":0}]},\"rx\":{\"type\":5,\"size\":0,\"loop\":1,\"seqCount\":1,\"seq\":[{\"pattern\":5,\"color\":8,\"time\":0,\"rate\":17,\"id\":0}]},\"dx\":{\"type\":13,\"size\":0,\"loop\":1,\"seqCount\":1,\"seq\":[{\"length\":0,\"id\":0,\"time\":8000}]},\"atx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"msg\":{\"seqCount\":0,\"seq\":[]},\"id\":0,\"vel2_shape\":false,\"fallstatus\":0,\"chargerStatus\":false}</expression>\n<expression>{\"tx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"ax\":{\"type\":0,\"size\":0,\"loop\":0,\"loop1\":0,\"seqCount\":0,\"seq\":[]},\"ax_stream\":{\"type\":15,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"mx\":{\"type\":4,\"size\":0,\"motion_type\":4,\"loop\":3,\"seqCount\":6,\"seq\":[{\"linear\":-850,\"angular\":0,\"time\":6,\"type\":1,\"id\":0},{\"linear\":850,\"angular\":0,\"time\":3,\"type\":1,\"id\":1},{\"linear\":-850,\"angular\":0,\"time\":3,\"type\":1,\"id\":2},{\"linear\":850,\"angular\":0,\"time\":3,\"type\":1,\"id\":3},{\"linear\":-850,\"angular\":0,\"time\":3,\"type\":1,\"id\":4},{\"linear\":850,\"angular\":0,\"time\":3,\"type\":1,\"id\":5}]},\"mx2\":{\"kp\":0,\"ki\":0,\"kd\":0,\"target_angle\":0,\"zonea\":0,\"zoneb\":0,\"positionScaleA\":0,\"positionScaleB\":0,\"positionScaleC\":0,\"velocityScaleStop\":0,\"velocityScaleMove\":0,\"onewheel\":0,\"falst\":0,\"mston\":0,\"mston_flag\":0,\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"steer\":0,\"seqCount\":0,\"seq\":[]},\"mx3\":{\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"ix1\":{\"type\":3,\"size\":0,\"imagetype\":1,\"loop\":0,\"seqCount\":1,\"seq\":[{\"frame\":\"b-90.png\",\"rate\":2,\"id\":0,\"loop\":0}]},\"rx\":{\"type\":5,\"size\":0,\"loop\":0,\"seqCount\":1,\"seq\":[{\"pattern\":5,\"color\":7,\"time\":0,\"rate\":17,\"id\":0}]},\"dx\":{\"type\":13,\"size\":0,\"loop\":1,\"seqCount\":1,\"seq\":[{\"length\":0,\"id\":0,\"time\":7000}]},\"atx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"msg\":{\"seqCount\":0,\"seq\":[]},\"id\":-928,\"vel2_shape\":false,\"fallstatus\":0,\"chargerStatus\":false}</expression>\n<expression>{\"tx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"ax\":{\"type\":0,\"size\":0,\"loop\":0,\"loop1\":0,\"seqCount\":0,\"seq\":[]},\"ax_stream\":{\"type\":15,\"size\":0,\"loop\":1,\"seqCount\":1,\"seq\":[{\"id\":0,\"title\":\"NEWS-2\",\"trackNumber\":0,\"totalTrackCount\":0,\"offsetStart\":\"-19746000\",\"offsetEnd\":\"-19689000\",\"site\":\"https://miko2.s3.ap-south-1.amazonaws.com/test/KidNuz_08_12_20+(1).mp3\"}]},\"mx\":{\"type\":4,\"size\":0,\"motion_type\":4,\"loop\":3,\"seqCount\":6,\"seq\":[{\"linear\":-850,\"angular\":0,\"time\":6,\"type\":1,\"id\":0},{\"linear\":850,\"angular\":0,\"time\":3,\"type\":1,\"id\":1},{\"linear\":-850,\"angular\":0,\"time\":3,\"type\":1,\"id\":2},{\"linear\":850,\"angular\":0,\"time\":3,\"type\":1,\"id\":3},{\"linear\":-850,\"angular\":0,\"time\":3,\"type\":1,\"id\":4},{\"linear\":850,\"angular\":0,\"time\":3,\"type\":1,\"id\":5}]},\"mx2\":{\"kp\":0,\"ki\":0,\"kd\":0,\"target_angle\":0,\"zonea\":0,\"zoneb\":0,\"positionScaleA\":0,\"positionScaleB\":0,\"positionScaleC\":0,\"velocityScaleStop\":0,\"velocityScaleMove\":0,\"onewheel\":0,\"falst\":0,\"mston\":0,\"mston_flag\":0,\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"steer\":0,\"seqCount\":0,\"seq\":[]},\"mx3\":{\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"ix1\":{\"type\":3,\"size\":0,\"imagetype\":1,\"loop\":0,\"seqCount\":1,\"seq\":[{\"frame\":\"b-90.png\",\"rate\":2,\"id\":0,\"loop\":0}]},\"rx\":{\"type\":5,\"size\":0,\"loop\":0,\"seqCount\":1,\"seq\":[{\"pattern\":5,\"color\":7,\"time\":0,\"rate\":17,\"id\":0}]},\"dx\":{\"type\":13,\"size\":0,\"loop\":1,\"seqCount\":1,\"seq\":[{\"length\":0,\"id\":0,\"time\":7000}]},\"atx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"msg\":{\"seqCount\":0,\"seq\":[]},\"id\":-928,\"vel2_shape\":false,\"fallstatus\":0,\"chargerStatus\":false}</expression>\n<expression>{\"tx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"ax\":{\"type\":0,\"size\":0,\"loop\":0,\"loop1\":0,\"seqCount\":0,\"seq\":[]},\"ax_stream\":{\"type\":15,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"mx\":{\"type\":4,\"size\":0,\"motion_type\":4,\"loop\":3,\"seqCount\":4,\"seq\":[{\"linear\":0,\"angular\":4,\"time\":1,\"type\":4,\"id\":0},{\"linear\":0,\"angular\":-5,\"time\":1,\"type\":4,\"id\":1},{\"linear\":0,\"angular\":5,\"time\":1,\"type\":4,\"id\":2},{\"linear\":0,\"angular\":-4,\"time\":1,\"type\":4,\"id\":3}]},\"mx2\":{\"kp\":0,\"ki\":0,\"kd\":0,\"target_angle\":0,\"zonea\":0,\"zoneb\":0,\"positionScaleA\":0,\"positionScaleB\":0,\"positionScaleC\":0,\"velocityScaleStop\":0,\"velocityScaleMove\":0,\"onewheel\":0,\"falst\":0,\"mston\":0,\"mston_flag\":0,\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"steer\":0,\"seqCount\":0,\"seq\":[]},\"mx3\":{\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"ix1\":{\"type\":3,\"size\":0,\"imagetype\":1,\"loop\":0,\"seqCount\":1,\"seq\":[{\"frame\":\"b-91.png\",\"rate\":0,\"id\":0,\"loop\":0}]},\"rx\":{\"type\":5,\"size\":0,\"loop\":1,\"seqCount\":1,\"seq\":[{\"pattern\":5,\"color\":8,\"time\":0,\"rate\":17,\"id\":0}]},\"dx\":{\"type\":13,\"size\":0,\"loop\":1,\"seqCount\":1,\"seq\":[{\"length\":0,\"id\":0,\"time\":8000}]},\"atx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"msg\":{\"seqCount\":0,\"seq\":[]},\"id\":0,\"vel2_shape\":false,\"fallstatus\":0,\"chargerStatus\":false}</expression>\n<expression>{\"tx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"ax\":{\"type\":0,\"size\":0,\"loop\":0,\"loop1\":0,\"seqCount\":0,\"seq\":[]},\"ax_stream\":{\"type\":15,\"size\":0,\"loop\":1,\"seqCount\":1,\"seq\":[{\"id\":0,\"title\":\"NEWS-3\",\"trackNumber\":0,\"totalTrackCount\":0,\"offsetStart\":\"-19689000\",\"offsetEnd\":\"-19659000\",\"site\":\"https://miko2.s3.ap-south-1.amazonaws.com/test/KidNuz_08_12_20+(1).mp3\"}]},\"mx\":{\"type\":4,\"size\":0,\"motion_type\":4,\"loop\":3,\"seqCount\":4,\"seq\":[{\"linear\":0,\"angular\":4,\"time\":1,\"type\":4,\"id\":0},{\"linear\":0,\"angular\":-5,\"time\":1,\"type\":4,\"id\":1},{\"linear\":0,\"angular\":5,\"time\":1,\"type\":4,\"id\":2},{\"linear\":0,\"angular\":-4,\"time\":1,\"type\":4,\"id\":3}]},\"mx2\":{\"kp\":0,\"ki\":0,\"kd\":0,\"target_angle\":0,\"zonea\":0,\"zoneb\":0,\"positionScaleA\":0,\"positionScaleB\":0,\"positionScaleC\":0,\"velocityScaleStop\":0,\"velocityScaleMove\":0,\"onewheel\":0,\"falst\":0,\"mston\":0,\"mston_flag\":0,\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"steer\":0,\"seqCount\":0,\"seq\":[]},\"mx3\":{\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"ix1\":{\"type\":3,\"size\":0,\"imagetype\":1,\"loop\":0,\"seqCount\":1,\"seq\":[{\"frame\":\"b-91.png\",\"rate\":0,\"id\":0,\"loop\":0}]},\"rx\":{\"type\":5,\"size\":0,\"loop\":1,\"seqCount\":1,\"seq\":[{\"pattern\":5,\"color\":8,\"time\":0,\"rate\":17,\"id\":0}]},\"dx\":{\"type\":13,\"size\":0,\"loop\":1,\"seqCount\":1,\"seq\":[{\"length\":0,\"id\":0,\"time\":8000}]},\"atx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"msg\":{\"seqCount\":0,\"seq\":[]},\"id\":0,\"vel2_shape\":false,\"fallstatus\":0,\"chargerStatus\":false}</expression>\n<expression>{\"tx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"ax\":{\"type\":0,\"size\":0,\"loop\":0,\"loop1\":0,\"seqCount\":0,\"seq\":[]},\"ax_stream\":{\"type\":15,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"mx\":{\"type\":4,\"size\":0,\"motion_type\":4,\"loop\":3,\"seqCount\":6,\"seq\":[{\"linear\":-850,\"angular\":0,\"time\":6,\"type\":1,\"id\":0},{\"linear\":850,\"angular\":0,\"time\":3,\"type\":1,\"id\":1},{\"linear\":-850,\"angular\":0,\"time\":3,\"type\":1,\"id\":2},{\"linear\":850,\"angular\":0,\"time\":3,\"type\":1,\"id\":3},{\"linear\":-850,\"angular\":0,\"time\":3,\"type\":1,\"id\":4},{\"linear\":850,\"angular\":0,\"time\":3,\"type\":1,\"id\":5}]},\"mx2\":{\"kp\":0,\"ki\":0,\"kd\":0,\"target_angle\":0,\"zonea\":0,\"zoneb\":0,\"positionScaleA\":0,\"positionScaleB\":0,\"positionScaleC\":0,\"velocityScaleStop\":0,\"velocityScaleMove\":0,\"onewheel\":0,\"falst\":0,\"mston\":0,\"mston_flag\":0,\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"steer\":0,\"seqCount\":0,\"seq\":[]},\"mx3\":{\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"ix1\":{\"type\":3,\"size\":0,\"imagetype\":1,\"loop\":0,\"seqCount\":1,\"seq\":[{\"frame\":\"b-97.png\",\"rate\":2,\"id\":0,\"loop\":0}]},\"rx\":{\"type\":5,\"size\":0,\"loop\":0,\"seqCount\":1,\"seq\":[{\"pattern\":5,\"color\":7,\"time\":0,\"rate\":17,\"id\":0}]},\"dx\":{\"type\":13,\"size\":0,\"loop\":1,\"seqCount\":1,\"seq\":[{\"length\":0,\"id\":0,\"time\":7000}]},\"atx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"msg\":{\"seqCount\":0,\"seq\":[]},\"id\":-928,\"vel2_shape\":false,\"fallstatus\":0,\"chargerStatus\":false}</expression>\n<expression>{\"tx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"ax\":{\"type\":0,\"size\":0,\"loop\":0,\"loop1\":0,\"seqCount\":0,\"seq\":[]},\"ax_stream\":{\"type\":15,\"size\":0,\"loop\":1,\"seqCount\":1,\"seq\":[{\"id\":0,\"title\":\"NEWS-4\",\"trackNumber\":0,\"totalTrackCount\":0,\"offsetStart\":\"-19659000\",\"offsetEnd\":\"-19611000\",\"site\":\"https://miko2.s3.ap-south-1.amazonaws.com/test/KidNuz_08_12_20+(1).mp3\"}]},\"mx\":{\"type\":4,\"size\":0,\"motion_type\":4,\"loop\":3,\"seqCount\":6,\"seq\":[{\"linear\":-850,\"angular\":0,\"time\":6,\"type\":1,\"id\":0},{\"linear\":850,\"angular\":0,\"time\":3,\"type\":1,\"id\":1},{\"linear\":-850,\"angular\":0,\"time\":3,\"type\":1,\"id\":2},{\"linear\":850,\"angular\":0,\"time\":3,\"type\":1,\"id\":3},{\"linear\":-850,\"angular\":0,\"time\":3,\"type\":1,\"id\":4},{\"linear\":850,\"angular\":0,\"time\":3,\"type\":1,\"id\":5}]},\"mx2\":{\"kp\":0,\"ki\":0,\"kd\":0,\"target_angle\":0,\"zonea\":0,\"zoneb\":0,\"positionScaleA\":0,\"positionScaleB\":0,\"positionScaleC\":0,\"velocityScaleStop\":0,\"velocityScaleMove\":0,\"onewheel\":0,\"falst\":0,\"mston\":0,\"mston_flag\":0,\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"steer\":0,\"seqCount\":0,\"seq\":[]},\"mx3\":{\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"ix1\":{\"type\":3,\"size\":0,\"imagetype\":1,\"loop\":0,\"seqCount\":1,\"seq\":[{\"frame\":\"b-97.png\",\"rate\":2,\"id\":0,\"loop\":0}]},\"rx\":{\"type\":5,\"size\":0,\"loop\":0,\"seqCount\":1,\"seq\":[{\"pattern\":5,\"color\":7,\"time\":0,\"rate\":17,\"id\":0}]},\"dx\":{\"type\":13,\"size\":0,\"loop\":1,\"seqCount\":1,\"seq\":[{\"length\":0,\"id\":0,\"time\":7000}]},\"atx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"msg\":{\"seqCount\":0,\"seq\":[]},\"id\":-928,\"vel2_shape\":false,\"fallstatus\":0,\"chargerStatus\":false}</expression>\n<expression>{\"tx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"ax\":{\"type\":0,\"size\":0,\"loop\":0,\"loop1\":0,\"seqCount\":0,\"seq\":[]},\"ax_stream\":{\"type\":15,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"mx\":{\"type\":4,\"size\":0,\"motion_type\":4,\"loop\":3,\"seqCount\":4,\"seq\":[{\"linear\":0,\"angular\":4,\"time\":1,\"type\":4,\"id\":0},{\"linear\":0,\"angular\":-5,\"time\":1,\"type\":4,\"id\":1},{\"linear\":0,\"angular\":5,\"time\":1,\"type\":4,\"id\":2},{\"linear\":0,\"angular\":-4,\"time\":1,\"type\":4,\"id\":3}]},\"mx2\":{\"kp\":0,\"ki\":0,\"kd\":0,\"target_angle\":0,\"zonea\":0,\"zoneb\":0,\"positionScaleA\":0,\"positionScaleB\":0,\"positionScaleC\":0,\"velocityScaleStop\":0,\"velocityScaleMove\":0,\"onewheel\":0,\"falst\":0,\"mston\":0,\"mston_flag\":0,\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"steer\":0,\"seqCount\":0,\"seq\":[]},\"mx3\":{\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"ix1\":{\"type\":3,\"size\":0,\"imagetype\":1,\"loop\":0,\"seqCount\":1,\"seq\":[{\"frame\":\"b-98.png\",\"rate\":0,\"id\":0,\"loop\":0}]},\"rx\":{\"type\":5,\"size\":0,\"loop\":1,\"seqCount\":1,\"seq\":[{\"pattern\":5,\"color\":8,\"time\":0,\"rate\":17,\"id\":0}]},\"dx\":{\"type\":13,\"size\":0,\"loop\":1,\"seqCount\":1,\"seq\":[{\"length\":0,\"id\":0,\"time\":8000}]},\"atx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"msg\":{\"seqCount\":0,\"seq\":[]},\"id\":0,\"vel2_shape\":false,\"fallstatus\":0,\"chargerStatus\":false}</expression>\n<expression>{\"tx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"ax\":{\"type\":0,\"size\":0,\"loop\":0,\"loop1\":0,\"seqCount\":0,\"seq\":[]},\"ax_stream\":{\"type\":15,\"size\":0,\"loop\":1,\"seqCount\":1,\"seq\":[{\"id\":0,\"title\":\"NEWS-5\",\"trackNumber\":0,\"totalTrackCount\":0,\"offsetStart\":\"-19611000\",\"offsetEnd\":\"-19590000\",\"site\":\"https://miko2.s3.ap-south-1.amazonaws.com/test/KidNuz_08_12_20+(1).mp3\"}]},\"mx\":{\"type\":4,\"size\":0,\"motion_type\":4,\"loop\":3,\"seqCount\":4,\"seq\":[{\"linear\":0,\"angular\":4,\"time\":1,\"type\":4,\"id\":0},{\"linear\":0,\"angular\":-5,\"time\":1,\"type\":4,\"id\":1},{\"linear\":0,\"angular\":5,\"time\":1,\"type\":4,\"id\":2},{\"linear\":0,\"angular\":-4,\"time\":1,\"type\":4,\"id\":3}]},\"mx2\":{\"kp\":0,\"ki\":0,\"kd\":0,\"target_angle\":0,\"zonea\":0,\"zoneb\":0,\"positionScaleA\":0,\"positionScaleB\":0,\"positionScaleC\":0,\"velocityScaleStop\":0,\"velocityScaleMove\":0,\"onewheel\":0,\"falst\":0,\"mston\":0,\"mston_flag\":0,\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"steer\":0,\"seqCount\":0,\"seq\":[]},\"mx3\":{\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"ix1\":{\"type\":3,\"size\":0,\"imagetype\":1,\"loop\":0,\"seqCount\":1,\"seq\":[{\"frame\":\"b-98.png\",\"rate\":0,\"id\":0,\"loop\":0}]},\"rx\":{\"type\":5,\"size\":0,\"loop\":1,\"seqCount\":1,\"seq\":[{\"pattern\":5,\"color\":8,\"time\":0,\"rate\":17,\"id\":0}]},\"dx\":{\"type\":13,\"size\":0,\"loop\":1,\"seqCount\":1,\"seq\":[{\"length\":0,\"id\":0,\"time\":8000}]},\"atx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"msg\":{\"seqCount\":0,\"seq\":[]},\"id\":0,\"vel2_shape\":false,\"fallstatus\":0,\"chargerStatus\":false}</expression>\n<expression>{\"tx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"ax\":{\"type\":0,\"size\":0,\"loop\":0,\"loop1\":0,\"seqCount\":0,\"seq\":[]},\"ax_stream\":{\"type\":15,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"mx\":{\"type\":4,\"size\":0,\"motion_type\":4,\"loop\":3,\"seqCount\":6,\"seq\":[{\"linear\":-850,\"angular\":0,\"time\":6,\"type\":1,\"id\":0},{\"linear\":850,\"angular\":0,\"time\":3,\"type\":1,\"id\":1},{\"linear\":-850,\"angular\":0,\"time\":3,\"type\":1,\"id\":2},{\"linear\":850,\"angular\":0,\"time\":3,\"type\":1,\"id\":3},{\"linear\":-850,\"angular\":0,\"time\":3,\"type\":1,\"id\":4},{\"linear\":850,\"angular\":0,\"time\":3,\"type\":1,\"id\":5}]},\"mx2\":{\"kp\":0,\"ki\":0,\"kd\":0,\"target_angle\":0,\"zonea\":0,\"zoneb\":0,\"positionScaleA\":0,\"positionScaleB\":0,\"positionScaleC\":0,\"velocityScaleStop\":0,\"velocityScaleMove\":0,\"onewheel\":0,\"falst\":0,\"mston\":0,\"mston_flag\":0,\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"steer\":0,\"seqCount\":0,\"seq\":[]},\"mx3\":{\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"ix1\":{\"type\":3,\"size\":0,\"imagetype\":1,\"loop\":0,\"seqCount\":1,\"seq\":[{\"frame\":\"b-99.png\",\"rate\":2,\"id\":0,\"loop\":0}]},\"rx\":{\"type\":5,\"size\":0,\"loop\":0,\"seqCount\":1,\"seq\":[{\"pattern\":5,\"color\":7,\"time\":0,\"rate\":15,\"id\":0}]},\"dx\":{\"type\":13,\"size\":0,\"loop\":1,\"seqCount\":1,\"seq\":[{\"length\":0,\"id\":0,\"time\":7000}]},\"atx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"msg\":{\"seqCount\":0,\"seq\":[]},\"id\":-928,\"vel2_shape\":false,\"fallstatus\":0,\"chargerStatus\":false}</expression>\n<expression>{\"tx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"ax\":{\"type\":0,\"size\":0,\"loop\":0,\"loop1\":0,\"seqCount\":0,\"seq\":[]},\"ax_stream\":{\"type\":15,\"size\":0,\"loop\":1,\"seqCount\":1,\"seq\":[{\"id\":0,\"title\":\"NEWS-6\",\"trackNumber\":0,\"totalTrackCount\":0,\"offsetStart\":\"-19590000\",\"offsetEnd\":\"-19566000\",\"site\":\"https://miko2.s3.ap-south-1.amazonaws.com/test/KidNuz_08_12_20+(1).mp3\"}]},\"mx\":{\"type\":4,\"size\":0,\"motion_type\":4,\"loop\":3,\"seqCount\":6,\"seq\":[{\"linear\":-850,\"angular\":0,\"time\":6,\"type\":1,\"id\":0},{\"linear\":850,\"angular\":0,\"time\":3,\"type\":1,\"id\":1},{\"linear\":-850,\"angular\":0,\"time\":3,\"type\":1,\"id\":2},{\"linear\":850,\"angular\":0,\"time\":3,\"type\":1,\"id\":3},{\"linear\":-850,\"angular\":0,\"time\":3,\"type\":1,\"id\":4},{\"linear\":850,\"angular\":0,\"time\":3,\"type\":1,\"id\":5}]},\"mx2\":{\"kp\":0,\"ki\":0,\"kd\":0,\"target_angle\":0,\"zonea\":0,\"zoneb\":0,\"positionScaleA\":0,\"positionScaleB\":0,\"positionScaleC\":0,\"velocityScaleStop\":0,\"velocityScaleMove\":0,\"onewheel\":0,\"falst\":0,\"mston\":0,\"mston_flag\":0,\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"steer\":0,\"seqCount\":0,\"seq\":[]},\"mx3\":{\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"ix1\":{\"type\":3,\"size\":0,\"imagetype\":1,\"loop\":0,\"seqCount\":1,\"seq\":[{\"frame\":\"b-99.png\",\"rate\":2,\"id\":0,\"loop\":0}]},\"rx\":{\"type\":5,\"size\":0,\"loop\":0,\"seqCount\":1,\"seq\":[{\"pattern\":5,\"color\":7,\"time\":0,\"rate\":15,\"id\":0}]},\"dx\":{\"type\":13,\"size\":0,\"loop\":1,\"seqCount\":1,\"seq\":[{\"length\":0,\"id\":0,\"time\":7000}]},\"atx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"msg\":{\"seqCount\":0,\"seq\":[]},\"id\":-928,\"vel2_shape\":false,\"fallstatus\":0,\"chargerStatus\":false}</expression></block>");
        speech.add("<block>\n<expression>{\"tx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":1,\"seq\":[{\"data\":\"Today,  World Day , for Audiovisual Heritage , is celebrated , all across the globe.\",\"length\":84,\"id\":0,\"time\":0,\"data_dsp\":\"Today, World Day  for Audiovisual Heritage  is celebrated  all across the globe.\",\"dsp_size\":0}]},\"ax\":{\"type\":0,\"size\":0,\"loop\":0,\"loop1\":0,\"seqCount\":0,\"seq\":[]},\"ax_stream\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"mx\":{\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"mx2\":{\"kp\":0,\"ki\":0,\"kd\":0,\"target_angle\":0,\"zonea\":0,\"zoneb\":0,\"positionScaleA\":0,\"positionScaleB\":0,\"positionScaleC\":0,\"velocityScaleStop\":0,\"velocityScaleMove\":0,\"onewheel\":0,\"falst\":0,\"mston\":0,\"mston_flag\":0,\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"steer\":0,\"seqCount\":0,\"seq\":[]},\"mx3\":{\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"ix1\":{\"type\":11,\"size\":0,\"imagetype\":0,\"loop\":1,\"seqCount\":1,\"seq\":[{\"frame\":\"blink.json\",\"rate\":0,\"id\":0,\"loop\":1}]},\"rx\":{\"type\":5,\"size\":24,\"loop\":0,\"seqCount\":1,\"seq\":[{\"pattern\":5,\"color\":8,\"time\":100,\"rate\":15,\"id\":0}]},\"dx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"atx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"msg\":{\"seqCount\":0,\"seq\":[]},\"id\":-407,\"vel2_shape\":false,\"fallstatus\":0,\"chargerStatus\":false}</expression></block>");
        speech.add("<block>\n<expression>{\"tx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":1,\"seq\":[{\"data\":\"A team of Indian , and Canadian researchers , discovered the earliest evidence , of dairy farming , in the Indus Valley Civilisation.\",\"length\":133,\"id\":0,\"time\":0,\"data_dsp\":\"A team of Indian  and Canadian researchers  discovered the earliest evidence  of dairy farming  in the Indus Valley Civilisation.\",\"dsp_size\":0}]},\"ax\":{\"type\":0,\"size\":0,\"loop\":0,\"loop1\":0,\"seqCount\":0,\"seq\":[]},\"ax_stream\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"mx\":{\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"mx2\":{\"kp\":0,\"ki\":0,\"kd\":0,\"target_angle\":0,\"zonea\":0,\"zoneb\":0,\"positionScaleA\":0,\"positionScaleB\":0,\"positionScaleC\":0,\"velocityScaleStop\":0,\"velocityScaleMove\":0,\"onewheel\":0,\"falst\":0,\"mston\":0,\"mston_flag\":0,\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"steer\":0,\"seqCount\":0,\"seq\":[]},\"mx3\":{\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"ix1\":{\"type\":11,\"size\":0,\"imagetype\":0,\"loop\":1,\"seqCount\":1,\"seq\":[{\"frame\":\"India_flag.png\",\"rate\":0,\"id\":0,\"loop\":1}]},\"rx\":{\"type\":5,\"size\":24,\"loop\":0,\"seqCount\":1,\"seq\":[{\"pattern\":5,\"color\":8,\"time\":100,\"rate\":15,\"id\":0}]},\"dx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"atx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"msg\":{\"seqCount\":0,\"seq\":[]},\"id\":-891,\"vel2_shape\":false,\"fallstatus\":0,\"chargerStatus\":false}</expression></block>");
        speech.add("<block>\n<expression>{\"tx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":1,\"seq\":[{\"data\":\"That\\u0027s all for now!\",\"length\":19,\"id\":0,\"time\":0,\"data_dsp\":\"That\\u0027s all for now!\",\"dsp_size\":0}]},\"ax\":{\"type\":0,\"size\":0,\"loop\":0,\"loop1\":0,\"seqCount\":0,\"seq\":[]},\"ax_stream\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"mx\":{\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"mx2\":{\"kp\":0,\"ki\":0,\"kd\":0,\"target_angle\":0,\"zonea\":0,\"zoneb\":0,\"positionScaleA\":0,\"positionScaleB\":0,\"positionScaleC\":0,\"velocityScaleStop\":0,\"velocityScaleMove\":0,\"onewheel\":0,\"falst\":0,\"mston\":0,\"mston_flag\":0,\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"steer\":0,\"seqCount\":0,\"seq\":[]},\"mx3\":{\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"ix1\":{\"type\":11,\"size\":0,\"imagetype\":0,\"loop\":1,\"seqCount\":1,\"seq\":[{\"frame\":\"very happy.json\",\"rate\":0,\"id\":0,\"loop\":1}]},\"rx\":{\"type\":5,\"size\":24,\"loop\":0,\"seqCount\":1,\"seq\":[{\"pattern\":12,\"color\":8,\"time\":100,\"rate\":75,\"id\":0}]},\"dx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"atx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"msg\":{\"seqCount\":0,\"seq\":[]},\"id\":569,\"vel2_shape\":false,\"fallstatus\":0,\"chargerStatus\":false}</expression></block>");


        speechArray = new ArrayList<>();
        long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(new java.util.Date().getTime());
        Log.d(TAG, "onCreate: " + diffInMinutes);
        /*button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speechArray = speech;
                axStreamModelArrayList = new ArrayList<>();
                for (int i = 0; i < speechArray.size(); i++) {
                    parseAIMLexpression(speechArray.get(i));
                }
                fillKeyFrames(seqArrayList,filteredBlockArray);
            }
        });*/



    }

    public static void writeStringAsFile(Context context, final String fileContents, String fileName) {

        try {
            android.util.Log.d(TAG, "writeStringAsFile: onDataReceived 1111");
            FileWriter out = new FileWriter(new File("/sdcard/Download/PRINT/", fileName));
            android.util.Log.d(TAG, "writeStringAsFile: onDataReceived 2222");
            out.write(fileContents);
            android.util.Log.d(TAG, "writeStringAsFile: onDataReceived 3333");
            out.close();
        } catch (IOException e) {
            android.util.Log.d(TAG, "writeStringAsFile: onDataReceived 4444");
            android.util.Log.e(TAG, "writeStringAsFile: ", e.getCause() );
        }
    }

    public void parseAIMLexpression(final String string2) {



        Log.e(TAG, "parseAIMLexpression " + string2);
        if (string2 != null && string2.length() > 0) {

            try {
                Log.e(TAG, "string is xml " + string2);
                DocumentBuilderFactory factory = DocumentBuilderFactory
                        .newInstance();

                DocumentBuilder builder = factory.newDocumentBuilder();
                String string = string2.replaceAll("[^\\x20-\\x7e]", "");

                StringBuilder xmlStringBuilder = new StringBuilder();
                xmlStringBuilder.append(string);
                ByteArrayInputStream input = new ByteArrayInputStream(
                        xmlStringBuilder.toString().getBytes("UTF-8"));
                org.w3c.dom.Document doc = builder.parse(input);

                if (!doc.getDocumentElement().getNodeName()
                        .equalsIgnoreCase("block")) {
                    Log.e("ZZ", "check data");
                    //System.out.println("check data");
                    return;
                }

                NodeList nList = doc.getElementsByTagName("expression");
                // Log.e("XX", "number of expressions :" + nList.getLength());
                Log.e(TAG, "number of expressions :"
                        + nList.getLength());
                for (int temp = 0; temp < nList.getLength(); temp++) {
                    Node nNode = nList.item(temp);


                    Log.e(TAG, "Node item: " + nList.item(temp));
                    Log.e(TAG, "Node type: " + nNode.getNodeType());
                    Log.e(TAG, "Node ELEMENT: " + Node.ELEMENT_NODE);
                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                        Node eElement = nNode;
                        NodeList n = ((Node) eElement).getChildNodes();
                        Log.e(TAG, "number of node elements :" + n.getLength());

                        for (int k = 0; k < n.getLength(); k++) {
                            Node n1 = n.item(k);
                            //Node n1e = n1;
                            String value = n1.getTextContent();
                            if (value.contains("ax_stream")) {
                                if(value.contains(".mp3")){
                                    Log.e(TAG, "value of expression string is :" + value);
                                    ExpressionModel badJson = new Gson().fromJson(value, ExpressionModel.class);
                                    Log.d(TAG, "parseAIMLexpression:badJson " + badJson.toString());
                                    seq = new Gson().fromJson(badJson.getAx_stream().getSeq().get(k).toString(), MediaMetaData.class);
                                    Log.d(TAG, "parseAIMLexpression:seq " + seq.toString());
//                                    filteredBlockArray.add(string2);
                                    seqArrayList.add(seq);
                                }
                            }
                        }
                    }


                }


            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "error in processing the xml" + e.getMessage());
            }
        }


    }

    // This is to create a json for playing expressions at specific time rate
    private ArrayList<ExpressionKeyFrame> fillKeyFrames(ArrayList<MediaMetaData> seqArrayList, ArrayList<String> filteredBlockArray) {
        expressionKeyFrameArrayList.clear();
        expressionKeyLinkedList = new LinkedList<ExpressionKeyFrame>();
        for (int i = 0; i < seqArrayList.size(); i++) {
            expressionKeyFrame = new ExpressionKeyFrame();
            expressionKeyFrame.setId(i);
            expressionKeyFrame.setStartTime(Long.parseLong(seqArrayList.get(i).getOffsetStart()));
            expressionKeyFrame.setEndTime(Long.parseLong(seqArrayList.get(i).getOffsetEnd()));
            expressionKeyFrame.setToDoTask(filteredBlockArray.get(i));
            expressionKeyLinkedList.add(expressionKeyFrame);
            expressionKeyFrameArrayList.add(expressionKeyFrame);
        }
        for(int i = 0; i < 7; i++) {
            try{
                switch (i){
                    case 0:
                        parseKeyFrames(1986);
                        break;
                    case 1:
                        parseKeyFrames(9987);
                        break;
                    case 2:
                        parseKeyFrames(24986);
                        break;
                    case 3:
                        parseKeyFrames(31986);
                        break;
                    case 4:
                        parseKeyFrames(38987);
                        break;
                    case 5:
                        parseKeyFrames(45987);
                        break;
                    case 6:
                        parseKeyFrames(49987);
                        break;
                    case 7:
                        parseKeyFrames(59988);
                        break;
                }

                Thread.sleep(2000);
            }catch(InterruptedException ex){
                //do stuff
            }
        }

        return expressionKeyFrameArrayList;
    }


    public void parseKeyFrames(long seekTimeInLong) {

        Iterator<ExpressionKeyFrame> iterator = expressionKeyLinkedList.iterator();
        while (iterator.hasNext()) {
            currentExpressionIndex = expressionKeyLinkedList.get(0).getStartTime();
            if (seekTimeInLong >= currentExpressionIndex) {

                currentExpressionIndex = iterator.next().getStartTime();
                Log.d(TAG, "parseKeyFrames:ss " + currentExpressionIndex);
                iterator.remove();
            }
        }
    }

/*
    public void parseAIMLexpression2(final String string2 ) {

        ArrayList<ExpressionMsg> b = new ArrayList<ExpressionMsg>();

        Log.e("TAG", "parseAIMLexpression " + string2);
        if (string2 != null && string2.length() > 0) {

            try {
                Log.e("TAG", "string is xml " + string2);
                DocumentBuilderFactory factory = DocumentBuilderFactory
                        .newInstance();

                DocumentBuilder builder = factory.newDocumentBuilder();
                String string = string2.replaceAll("[^\\x20-\\x7e]", "");

                StringBuilder xmlStringBuilder = new StringBuilder();
                xmlStringBuilder.append(string);
                ByteArrayInputStream input = new ByteArrayInputStream(
                        xmlStringBuilder.toString().getBytes("UTF-8"));
                org.w3c.dom.Document doc = builder.parse(input);

                if (!doc.getDocumentElement().getNodeName()
                        .equalsIgnoreCase("block")) {
                    Log.e("ZZ", "check data");
                    //System.out.println("check data");
                    return;
                }
                if(b!=null)
                {
                    b.clear();
                }
                NodeList nList = doc.getElementsByTagName("expression");
                // Log.e("XX", "number of expressions :" + nList.getLength());
                Log.e("TAG", "number of expressions :"
                        + nList.getLength());
                for (int temp = 0; temp < nList.getLength(); temp++) {
                    Node nNode = nList.item(temp);
                    Log.e(TAG, "Node item: " + nList.item(temp));
                    Log.e(TAG, "Node type: " + nNode.getNodeType());
                    Log.e(TAG, "Node ELEMENT: " + Node.ELEMENT_NODE);
                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                        Node eElement = nNode;
                        NodeList n = ((Node) eElement).getChildNodes();
                        Log.e("TAG", "number of node elements :" + n.getLength());

                        for (int k = 0; k < n.getLength(); k++) {
                            Node n1 = n.item(k);
                            //Node n1e = n1;
                            String value = n1.getTextContent();
                            Log.e("TAG", "value of expression string is :" + value);
                            if (value.contains("ax_stream")) {
                                badJson badJson = new Gson().fromJson(value, badJson.class);
                                Seq seq = new Gson().fromJson(badJson.getAx_stream().getSeq().get(k).toString(), Seq.class);
                                Log.d(TAG, "parseAIMLexpression2 111 : " + seq.toString());
                            }

                        }

                    }


                }


            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "error in processing the xml" + e.getMessage());
            }
        }
    }

    public static AxStreamModel loadExpressionString(String s) {
        Gson gson;
        gson = new GsonBuilder().create();
        AxStreamModel expressionMsg = gson.fromJson(s, AxStreamModel.class);
        return expressionMsg;
    }*/

}
