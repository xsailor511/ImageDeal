package richard.ztesoft.com.imagedeal;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @InjectView(R.id.image_normal)
    ImageView image_normal;
    @InjectView(R.id.transform_button)
    Button transform_botto;
    @InjectView(R.id.image_new)
    ImageView image_new;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        image_normal.setImageResource(R.drawable.baidu_res_split_line_box_804);
    }

    @OnClick(R.id.transform_button)
    public void onClicked(){
        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(),R.drawable.baidu_res_split_line_box_804);
        Bitmap bitmap1 = transfromBitmap(bitmap);

        image_new.setImageBitmap(bitmap1);
    }

    private Bitmap transfromBitmap(Bitmap bitmap){
        Bitmap show = bitmap; //这就是原始的图片
        int flag = 0x07; // 比特位0 表示是否改变色相，比位1表示是否改变饱和度,比特位2表示是否改变明亮度
        int wi =show.getWidth(); //得到宽度
        int he =show.getHeight(); //得到高度
        Bitmap bmp =Bitmap.createBitmap(wi, he,Bitmap.Config.ARGB_8888);
        //创建一个相同尺寸的可变的位图区,用于绘制调色后的图片
        Canvas canvas = new Canvas(bmp); //得到画笔对象
        Paint paint = new Paint(); //新建paint
        paint.setAntiAlias(true); //设置抗锯齿,也即是边缘做平滑处理
        ColorMatrix cm1=new ColorMatrix(); //用于颜色变换的矩阵，android 位图颜色变化处理主要是靠该对象完成
        ColorMatrix cm2=new ColorMatrix();
        ColorMatrix cm3=new ColorMatrix();
        cm1.reset(); //设为默认值

        float saturation = 0f;
        float hueColor = 1f;
        if ((flag & 0x01) !=0) //需要改变色相
        {
        //hueColor就是色轮旋转的角度,正值表示顺时针旋转，负值表示逆时针旋转
            cm1.setRotate(0, hueColor); //控制让红**在色轮上旋转hueColor葛角度
            cm1.setRotate(1, hueColor); //控制让绿红**在色轮上旋转hueColor葛角度
            cm1.setRotate(2, hueColor); //控制让蓝**在色轮上旋转hueColor葛角度
        //这里相当于改变的是全图的色相
        }
        if ((flag & 0x02) !=0) //需要改变饱和度
        {
        //saturation 饱和度值，最小可设为0，此时对应的是灰度图(也就是俗话的“黑白图”)，
        //为1表示饱和度不变，设置大于1，就显示过饱和
            cm2.reset();
            cm2.setSaturation(saturation);
            cm1.postConcat(cm2); //效果叠加
        }

        float rScale = 0.5f,gScale = 0.5f,bScale = 0.5f;

        if ((flag & 0x04) !=0)
        {
        //f 表示亮度比例，取值小于1，表示亮度减弱，否则亮度增强
            cm3.reset();
            cm3.setScale(rScale, gScale, bScale, 1); //红、绿、蓝三分量按相同的比例,最后一个参数1表示透明度不做变化，此函数详细说明参考 android doc
            cm1.postConcat(cm3); //效果叠加
        }
        paint.setColorFilter(new ColorMatrixColorFilter(cm1));//设置颜色变换效果
        canvas.drawBitmap(show,0, 0, paint); //将颜色变化后的图片输出到新创建的位图区
        return bmp; //返回新的位图，也即调色处理后的图片
    }
}
