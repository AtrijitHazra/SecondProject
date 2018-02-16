package firebase.demo.firebaseapplicationdemo;

import android.content.Intent;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.FirebaseApp;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPTableHeader;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import firebase.demo.firebaseapplicationdemo.model.PdfTestUser;
import firebase.demo.firebaseapplicationdemo.model.PdfTestUserAddress;
import firebase.demo.firebaseapplicationdemo.model.PdfTestUsermobile;

public class MainActivity extends AppCompatActivity {

    String TAG = "MainActivityNew";
    private static final int RC_SIGN_PHONE = 9002;

    ArrayList<PdfTestUser> userName = new ArrayList<>();
    ArrayList<PdfTestUsermobile> userMobile = new ArrayList<>();
    ArrayList<PdfTestUserAddress> userAddresses = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(getApplicationContext());

    }

    public void signInWithGoogle(View v) {
        startActivity(new Intent(MainActivity.this, GoogleAuth.class));
    }

    public void signInWithfacebook(View v) {
//        startActivity(new Intent(MainActivity.this,FacebookAuth.class));
        CallbackManager mCallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = findViewById(R.id.fb_login);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                Toast.makeText(MainActivity.this, " loginResult " + loginResult, Toast.LENGTH_SHORT).show();
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.v("LoginActivity", response.toString());
                                try {
                                    String id = object.getString("id").trim();
                                    String name = object.getString("name").trim();
                                    String email = object.getString("email").trim();
                                    String image = "https://graph.facebook.com/" + id + "/picture?type=large";
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle bundle = new Bundle();
                bundle.putString("fields", "id,name,email,gender,picture.type(large)");
                request.setParameters(bundle);
                request.executeAsync();
                LoginManager.getInstance().logOut();
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                // ...
            }
        });
    }

    public void signInWithTwitter(View v) {
        startActivity(new Intent(MainActivity.this, TwitterAuth.class));
    }

    public void signInWithNumber(View v) {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(
                                Arrays.asList(
                                        new AuthUI.IdpConfig.Builder(AuthUI.PHONE_VERIFICATION_PROVIDER).build()
                                ))
                        .build(),
                RC_SIGN_PHONE);
    }

    public void signInWithEmail(View v) {
        startActivity(new Intent(MainActivity.this, EmailAuth.class));
    }

    public void uploadFile(View v) {
        startActivity(new Intent(MainActivity.this, PdfDisplay.class));
    }

    public void createPdf(View v)  throws IOException, BadElementException{
        setPdfTestDoc();
        createPdf();
    }

    public void createTreeView(View v)
    {
        startActivity(new Intent(MainActivity.this, TreeStructure.class));
    }

    public void setPdfTestDoc() {

        PdfTestUser user = new PdfTestUser();
        PdfTestUser user1 = new PdfTestUser();
        PdfTestUser user2 = new PdfTestUser();
        user.setName("Atrijit");
        user1.setName("Happy");
        user2.setName("Arindam");
        userName.add(user);
        userName.add(user1);
        userName.add(user2);

        PdfTestUsermobile userM = new PdfTestUsermobile();
        PdfTestUsermobile userM1 = new PdfTestUsermobile();
        PdfTestUsermobile userM2 = new PdfTestUsermobile();
        userM.setMobile("7044985628");
        userM1.setMobile("8240183824");
        userM2.setMobile("9876543210");
        userMobile.add(userM);
        userMobile.add(userM1);
        userMobile.add(userM2);

        PdfTestUserAddress userA = new PdfTestUserAddress();
        PdfTestUserAddress userA1 = new PdfTestUserAddress();
        PdfTestUserAddress userA2 = new PdfTestUserAddress();
        userA.setAddress("Uttarpara");
        userA1.setAddress("Malda");
        userA2.setAddress("Kolkata");
        userAddresses.add(userA);
        userAddresses.add(userA1);
        userAddresses.add(userA2);


    }

    public void createPdf() throws IOException, BadElementException {

        String FILE = Environment.getExternalStorageDirectory() + File.separator
                + "HelloWorld.pdf";
        File file = new File(FILE);
        if (!file.exists())
            file.createNewFile();

        Rectangle pageSize = new Rectangle(PageSize.A4);
        pageSize.setBackgroundColor(BaseColor.YELLOW);
        Document document = new Document(pageSize);
        PdfWriter writer = null;
        try {
            writer = PdfWriter.getInstance(document, new FileOutputStream(FILE));
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        document.open();

        Font chapterFont = FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD);
        Font paragraphFont = FontFactory.getFont(FontFactory.HELVETICA, 14, Font.NORMAL);
        Chunk chunk = new Chunk("Tax Invoice - Buyer's choice", chapterFont);
        Chapter chapter = new Chapter(new Paragraph(chunk), 1);
        chapter.setNumberDepth(0);
        Paragraph tax_invoice = new Paragraph("Tax Invoice - Buyer's copy", paragraphFont);
        tax_invoice.setAlignment(Element.ALIGN_RIGHT);
//        chapter.add(new Paragraph("This is the paragraph", paragraphFont));

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Image company_image = Image.getInstance("https://i.ebayimg.com/images/g/nKQAAOSwg8tZ81CX/s-l300.jpg");
        float scaler = ((document.getPageSize().getWidth() - document.leftMargin()
                - document.rightMargin() - 0) / company_image.getWidth()) * 100;

//        company_image.scalePercent(scaler);
        company_image.scaleAbsolute((document.getPageSize().getWidth() - document.leftMargin()
                - document.rightMargin() - 0), 100);
        company_image.setBorder(Rectangle.BOTTOM | Rectangle.RIGHT | Rectangle.TOP | Rectangle.LEFT);

        PdfPTable purchaser_details = new PdfPTable(new float[]{1, 1});
        purchaser_details.setWidthPercentage(100);

        PdfPCell purchaser_address = new PdfPCell();
        purchaser_address.addElement(new Phrase("Billing To : ", chapterFont));
        purchaser_address.addElement(new Phrase("Atrijit Hazra", chapterFont));
        purchaser_address.addElement(new Phrase("Nabadwip,Nadia \nWEST BENGAL", chapterFont));
        purchaser_address.setBorder(Rectangle.BOTTOM | Rectangle.RIGHT | Rectangle.TOP | Rectangle.LEFT);
        purchaser_address.setPadding(10);

        PdfPCell invoice_number_details = new PdfPCell();
        invoice_number_details.addElement(new Phrase("Tax Invoice Number : VIPL/INV/17-18/811071/16 ", chapterFont));
        invoice_number_details.addElement(new Phrase("Warranty: 1 Year ", chapterFont));
        invoice_number_details.addElement(new Phrase("Date : 20/01/2018\n", chapterFont));
        invoice_number_details.setBorder(Rectangle.BOTTOM | Rectangle.RIGHT | Rectangle.TOP | Rectangle.LEFT);
        invoice_number_details.setPadding(10);
        invoice_number_details.setHorizontalAlignment(Element.ALIGN_RIGHT);

        purchaser_details.addCell(purchaser_address);
        purchaser_details.addCell(invoice_number_details);


        PdfPTable item_table = new PdfPTable(new float[]{1, 5, 2, 2, 2, 2, 2});
        item_table.setTotalWidth(document.getPageSize().getWidth() - document.leftMargin() - document.rightMargin());
        item_table.setWidthPercentage(100);
        item_table.setLockedWidth(true);

        PdfPCell sl_no = new PdfPCell(new Phrase("Sl.", chapterFont));
        sl_no.setFixedHeight(40);
        sl_no.setPadding(5);
        sl_no.setBorder(Rectangle.BOTTOM | Rectangle.RIGHT | Rectangle.TOP | Rectangle.LEFT);
        item_table.setHeaderRows(1);
        item_table.addCell(sl_no);

        PdfPCell product_name = new PdfPCell(new Phrase("Product/Service", chapterFont));
        product_name.setFixedHeight(40);
        product_name.setPadding(5);
        product_name.setBorder(Rectangle.BOTTOM | Rectangle.RIGHT | Rectangle.TOP | Rectangle.LEFT);
        item_table.setHeaderRows(1);
        item_table.addCell(product_name);

        PdfPCell hsn_asc = new PdfPCell(new Phrase("HSN/SAC", chapterFont));
        hsn_asc.setFixedHeight(40);
        hsn_asc.setPadding(5);
        hsn_asc.setBorder(Rectangle.BOTTOM | Rectangle.RIGHT | Rectangle.TOP | Rectangle.LEFT);
        item_table.setHeaderRows(1);
        item_table.addCell(hsn_asc);

        PdfPCell price_item = new PdfPCell(new Phrase("Price", chapterFont));
        price_item.setFixedHeight(40);
        price_item.setPadding(5);
        price_item.setBorder(Rectangle.BOTTOM | Rectangle.RIGHT | Rectangle.TOP | Rectangle.LEFT);
        item_table.setHeaderRows(1);
        item_table.addCell(price_item);

        PdfPCell qty_item = new PdfPCell(new Phrase("QTY", chapterFont));
        qty_item.setFixedHeight(40);
        qty_item.setPadding(5);
        qty_item.setBorder(Rectangle.BOTTOM | Rectangle.RIGHT | Rectangle.TOP | Rectangle.LEFT);
        item_table.setHeaderRows(1);
        item_table.addCell(qty_item);

        PdfPCell taxable = new PdfPCell(new Phrase("Taxable", chapterFont));
        taxable.setFixedHeight(40);
        taxable.setPadding(5);
        taxable.setBorder(Rectangle.BOTTOM | Rectangle.RIGHT | Rectangle.TOP | Rectangle.LEFT);
        item_table.setHeaderRows(1);
        item_table.addCell(taxable);

        PdfPCell gst_item = new PdfPCell(new Phrase("GST", chapterFont));
        gst_item.setFixedHeight(40);
        gst_item.setPadding(5);
        gst_item.setBorder(Rectangle.BOTTOM | Rectangle.RIGHT | Rectangle.TOP | Rectangle.LEFT);
        item_table.setHeaderRows(1);
        item_table.addCell(gst_item);

        PdfPCell username;

        for (int i = 0; i < 9; i++) {
            username = new PdfPCell(new Phrase("" + (i + 1), paragraphFont));
            username.setFixedHeight(30);
            username.setVerticalAlignment(Element.ALIGN_MIDDLE);
            username.setBorder(Rectangle.BOTTOM);
            username.setPadding(5);
            username.setBorder(Rectangle.BOTTOM | Rectangle.RIGHT | Rectangle.TOP | Rectangle.LEFT);
            username.setBorderColor(BaseColor.BLACK);
            item_table.addCell(username);

            username = new PdfPCell(new Phrase("Hikvision - DVR-4CH(P)", paragraphFont));
            username.setFixedHeight(30);
            username.setVerticalAlignment(Element.ALIGN_MIDDLE);
            username.setBorder(Rectangle.BOTTOM);
            username.setPadding(5);
            username.setBorder(Rectangle.BOTTOM | Rectangle.RIGHT | Rectangle.TOP | Rectangle.LEFT);
            username.setBorderColor(BaseColor.BLACK);
            item_table.addCell(username);

            username = new PdfPCell(new Phrase("8521", paragraphFont));
            username.setFixedHeight(30);
            username.setVerticalAlignment(Element.ALIGN_MIDDLE);
            username.setBorder(Rectangle.BOTTOM);
            username.setPadding(5);
            username.setBorder(Rectangle.BOTTOM | Rectangle.RIGHT | Rectangle.TOP | Rectangle.LEFT);
            username.setBorderColor(BaseColor.BLACK);
            item_table.addCell(username);

            username = new PdfPCell(new Phrase("2860.00", paragraphFont));
            username.setFixedHeight(30);
            username.setVerticalAlignment(Element.ALIGN_MIDDLE);
            username.setBorder(Rectangle.BOTTOM);
            username.setPadding(5);
            username.setBorder(Rectangle.BOTTOM | Rectangle.RIGHT | Rectangle.TOP | Rectangle.LEFT);
            username.setBorderColor(BaseColor.BLACK);
            item_table.addCell(username);

            username = new PdfPCell(new Phrase("1 Pcs", paragraphFont));
            username.setFixedHeight(30);
            username.setVerticalAlignment(Element.ALIGN_MIDDLE);
            username.setBorder(Rectangle.BOTTOM);
            username.setPadding(5);
            username.setBorder(Rectangle.BOTTOM | Rectangle.RIGHT | Rectangle.TOP | Rectangle.LEFT);
            username.setBorderColor(BaseColor.BLACK);
            item_table.addCell(username);

            username = new PdfPCell(new Phrase("2,860", paragraphFont));
            username.setFixedHeight(30);
            username.setVerticalAlignment(Element.ALIGN_MIDDLE);
            username.setBorder(Rectangle.BOTTOM);
            username.setPadding(5);
            username.setBorder(Rectangle.BOTTOM | Rectangle.RIGHT | Rectangle.TOP | Rectangle.LEFT);
            username.setBorderColor(BaseColor.BLACK);
            item_table.addCell(username);

            username = new PdfPCell(new Phrase("18.00%"));
            username.setFixedHeight(30);
            username.setVerticalAlignment(Element.ALIGN_MIDDLE);
            username.setBorder(Rectangle.BOTTOM);
            username.setPadding(5);
            username.setBorder(Rectangle.BOTTOM | Rectangle.RIGHT | Rectangle.TOP | Rectangle.LEFT);
            username.setBorderColor(BaseColor.BLACK);
            item_table.addCell(username);

        }

        PdfPTable total_bill_table = new PdfPTable(new float[]{12, 2, 2});
        total_bill_table.setTotalWidth(document.getPageSize().getWidth() - document.leftMargin() - document.rightMargin());
        total_bill_table.setWidthPercentage(100);
        total_bill_table.setLockedWidth(true);

        PdfPCell total_text = new PdfPCell(new Phrase("Total:", chapterFont));
        total_text.setPadding(5);
        total_text.setBorder(Rectangle.BOTTOM | Rectangle.RIGHT | Rectangle.TOP | Rectangle.LEFT);
        total_bill_table.addCell(total_text);

        PdfPCell total_amount = new PdfPCell(new Phrase("11,510", chapterFont));
        total_amount.setPadding(5);
        total_amount.setBorder(Rectangle.BOTTOM | Rectangle.RIGHT | Rectangle.TOP | Rectangle.LEFT);
        total_bill_table.addCell(total_amount);

        PdfPCell total_gst_bill = new PdfPCell();
        total_gst_bill.setPadding(5);
        total_gst_bill.setBorder(Rectangle.BOTTOM | Rectangle.RIGHT | Rectangle.TOP | Rectangle.LEFT);
        total_gst_bill.addElement(new Phrase("CGST :", paragraphFont));
        total_gst_bill.addElement(new Phrase("  1035.9", paragraphFont));
        total_gst_bill.addElement(new Phrase("IGST :", chapterFont));
        total_gst_bill.addElement(new Phrase("  1035.9", paragraphFont));
        total_bill_table.addCell(total_gst_bill);

        PdfPTable total_bill_in_text = new PdfPTable(new float[]{12, 4});
        total_bill_in_text.setTotalWidth(document.getPageSize().getWidth() - document.leftMargin() - document.rightMargin());
        total_bill_in_text.setWidthPercentage(100);
        total_bill_in_text.setLockedWidth(true);

        PdfPCell payable_amount_text = new PdfPCell(new Phrase("Total Payable Amount (Taxable Amount + GST Amount) :", chapterFont));
        payable_amount_text.setPadding(5);
        payable_amount_text.setBorder(Rectangle.BOTTOM | Rectangle.RIGHT | Rectangle.TOP | Rectangle.LEFT);
        total_bill_in_text.addCell(payable_amount_text);

        PdfPCell payable_amount = new PdfPCell(new Phrase("13,582", chapterFont));
        payable_amount.setPadding(5);
        payable_amount.setBorder(Rectangle.BOTTOM | Rectangle.RIGHT | Rectangle.TOP | Rectangle.LEFT);
        total_bill_in_text.addCell(payable_amount);

        PdfPTable amount_in_word = new PdfPTable(1);
        amount_in_word.setTotalWidth(document.getPageSize().getWidth() - document.leftMargin() - document.rightMargin());
        amount_in_word.setWidthPercentage(100);
        amount_in_word.setLockedWidth(true);

        PdfPCell amount_word = new PdfPCell(new Phrase("Pay Amount in Words : Indian Rupees Thirteen Thousand Five Hundred & Eighty Two Only.", chapterFont));
        amount_word.setPadding(5);
        amount_word.setBorder(Rectangle.BOTTOM | Rectangle.RIGHT | Rectangle.TOP | Rectangle.LEFT);
        amount_in_word.addCell(amount_word);

        PdfPTable terms_n_cond = new PdfPTable(1);
        terms_n_cond.setTotalWidth(document.getPageSize().getWidth() - document.leftMargin() - document.rightMargin());
        terms_n_cond.setWidthPercentage(100);
        terms_n_cond.setLockedWidth(true);

        LineSeparator line = new LineSeparator(1, 100, null, Element.ALIGN_CENTER, -2);

        PdfPCell terms_text = new PdfPCell();
        terms_text.setPadding(5);
        terms_text.setBorder(Rectangle.BOTTOM | Rectangle.RIGHT | Rectangle.TOP | Rectangle.LEFT);
        Chunk chunk1 = new Chunk("Terms & Conditions :",chapterFont);
        chunk1.setUnderline(1.5f, -1);
        terms_text.addElement(new Phrase(chunk1));
        terms_text.addElement(new Phrase(Chunk.NEWLINE));
        terms_text.addElement(new Phrase("1.Payment to be made as per invoice.", paragraphFont));
        terms_text.addElement(new Phrase("2.Please pay by A/C payee cheque or NEFT only.", paragraphFont));
        terms_text.addElement(new Phrase("3.Warranty of all items is covered by the principle.", paragraphFont));
        terms_text.addElement(new Phrase("4.No warranty provide on any accidental damage.", paragraphFont));
        terms_text.addElement(new Phrase("5.All dispute will be subject to Kolkata Jurisdiction.", paragraphFont));
        terms_n_cond.addCell(terms_text);


        try {
            document.add(tax_invoice);
//            document.add(Chunk.NEWLINE);
            document.add(company_image);
            document.add(purchaser_details);
            document.add(item_table);
            document.add(total_bill_table);
            document.add(total_bill_in_text);
            document.add(amount_in_word);
            document.add(terms_n_cond);
//            document.add(tablewee);
            document.close();
            Toast.makeText(this, "Pdf Created ", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, PdfDisplay.class));
        } catch (DocumentException e) {
            e.printStackTrace();
            document.close();
        }

    }
}
