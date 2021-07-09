package USER;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.covidcare.HomeActivity;
import com.example.covidcare.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.itextpdf.barcodes.BarcodeQRCode;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class confirmation2 extends AppCompatActivity {
    String hospital, city, bed, newbed, gmail, parts11,tdaydate,tdaytime,tdayname,address,patientname,patientage;
    String oxygenbed, newoxybed, newoxygen, newnormal,dell;
    int i, a, oxy, norm;
    Button oxybutton;
    TextView oxygen, normal,oxydate,oxytime,oxyname,oxyaddress;
    DatabaseReference databaseReference;
    DocumentReference reference;
    DatabaseReference profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation2);
        hospital = getIntent().getStringExtra("hospital");
        city = getIntent().getStringExtra("city");
        gmail = getIntent().getStringExtra("email");
        tdaydate=getIntent().getStringExtra("date");
        tdayname=getIntent().getStringExtra("time");
        address=getIntent().getStringExtra("address");
        patientage=getIntent().getStringExtra("age");
        patientname=getIntent().getStringExtra("name");
        dell=getIntent().getStringExtra("oxygen");
        profile=FirebaseDatabase.getInstance().getReference().child("Update").child(dell).child("Date");
        LocalDate localDate=LocalDate.now();
        LocalDate Threeday=localDate.plusDays(3);
        String threeday=Threeday.toString();
        profile.setValue(threeday);
        String[] parts = gmail.split("(?=@)");
        parts11 = parts[0];
        oxytime = findViewById(R.id.oxytime);
        oxydate = findViewById(R.id.oxydate);
        oxyname = findViewById(R.id.oxyname);
        oxyaddress = findViewById(R.id.oxyaddress);
        oxybutton=findViewById(R.id.oxybutton);
        oxytime.setText(tdayname);
        oxydate.setText(tdaydate);
        oxyname.setText(hospital);
        oxyaddress.setText(address);
        databaseReference= FirebaseDatabase.getInstance().getReference("GMAIL OF HOSPITALS").child(parts11);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                newoxygen= snapshot.child("Total no of Oxygen Beds").getValue(String.class);
                oxy=Integer.parseInt(newoxygen);
                newnormal=String.valueOf(norm);
                if (oxy<=0){
                    oxy=0;
                }else {
                    oxy=oxy-1;
                }

                newoxybed=String.valueOf(oxy);
                snapshot.getRef().child("Total no of Oxygen Beds").setValue(newoxybed);





            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        reference= FirebaseFirestore.getInstance().collection(city).document(hospital);
        reference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                oxygenbed = documentSnapshot.getString("Total no of Oxygen Beds");
                a = Integer.parseInt(oxygenbed);
                if (a <= 0) {
                    a = 0;
                } else {
                    a = a - 1;
                }
                newoxybed = String.valueOf(a);


                Map<String, Object> data = new HashMap<>();

                data.put("Total no of Oxygen Beds", newoxybed);
                reference.update(data);

            }
        });
        oxybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
                intent.putExtra("gmailid",gmail);
                finish();
            }
        });
        downloadpdf();

    }

    private void downloadpdf() {
        try {
            createPDF(patientname,patientage,hospital,address,tdayname,tdaydate);
        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
        }
    }

    private void createPDF(String username,String userage,String hospital,String address,String time,String date) throws FileNotFoundException {
        String pdpath= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        double b = (double) (Math.random()*(99999-1+1)+1);
        String s=Double. toString(b);
        File file=new File(pdpath,s.concat("o2bed.pdf"));
        OutputStream outputStream=new FileOutputStream(file);
        PdfWriter writer=new PdfWriter(file);
        PdfDocument pdfDocument=new PdfDocument(writer);
        Document document=new Document(pdfDocument);

        pdfDocument.setDefaultPageSize(PageSize.A5);
        document.setMargins(0,0,0,0);
        Drawable d=getDrawable(R.drawable.image123);
        Bitmap bitmap=((BitmapDrawable)d).getBitmap();
        ByteArrayOutputStream stream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
        byte[] bitmapData=stream.toByteArray();
        ImageData imageData= ImageDataFactory.create(bitmapData);
        Image image=new Image(imageData);


        Paragraph varanasi=new Paragraph("BOOKING DETAILS").setBold().setFontSize(20).setTextAlignment(TextAlignment.CENTER);


        float[] width={100f,100f};
        Table table=new Table(width);
        table.setHorizontalAlignment(HorizontalAlignment.CENTER);

        table.addCell(new Cell().add(new Paragraph("Patient Name")));
        table.addCell(new Cell().add(new Paragraph(username)));

        table.addCell(new Cell().add(new Paragraph("Patient Age")));
        table.addCell(new Cell().add(new Paragraph(userage)));

        table.addCell(new Cell().add(new Paragraph("Hospital Name")));
        table.addCell(new Cell().add(new Paragraph(hospital)));

        table.addCell(new Cell().add(new Paragraph("Hospital Address")));
        table.addCell(new Cell().add(new Paragraph(address)));

        table.addCell(new Cell().add(new Paragraph("Date")));
        table.addCell(new Cell().add(new Paragraph(date)));

        table.addCell(new Cell().add(new Paragraph("Time")));
        table.addCell(new Cell().add(new Paragraph(time)));

        table.addCell(new Cell().add(new Paragraph("Bed Booked")));
        table.addCell(new Cell().add(new Paragraph("Oxygen Bed")));

        BarcodeQRCode qrCode=new BarcodeQRCode(username+"\n"+userage+"\n"+hospital+"\n"+address+"\n"+date+"\n"+time);
        PdfFormXObject qrCodeobject=qrCode.createFormXObject(ColorConstants.BLACK,pdfDocument);
        Image qrcodeimage=new Image(qrCodeobject).setWidth(80).setHorizontalAlignment(HorizontalAlignment.CENTER);

        document.add(image);

        document.add(varanasi);
        document.add(table);
        document.add(qrcodeimage);
        document.close();
        Toast.makeText(this, "PDF created", Toast.LENGTH_SHORT).show();
    }





}