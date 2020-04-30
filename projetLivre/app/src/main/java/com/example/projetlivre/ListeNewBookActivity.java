package com.example.projetlivre;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.projetlivre.dataB.BookViewModel;
import com.example.projetlivre.listadapter.BookListAdapter;
import com.example.projetlivre.object.Book;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.Properties;
import javax.servlet.http.*;
import javax.mail.*;
import javax.mail.internet.*;

public class ListeNewBookActivity extends AppCompatActivity {

    public static BookViewModel mBookViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newliste_livres);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final BookListAdapter adapter = new BookListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton retour = findViewById(R.id.retour);
        retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( ListeNewBookActivity.this, NewBookActivity.class);
                startActivity(intent);
            }
        });

        FloatingActionButton send = findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast up = Toast.makeText(getApplicationContext(), "UPLOAD !", Toast.LENGTH_SHORT);
                up.show();
            }
        });

        mBookViewModel = ViewModelProviders.of(this).get(BookViewModel.class);
        mBookViewModel.getmAllBooks().observe(this, new Observer<List<Book>>() {
            @Override
            public void onChanged(@Nullable final List<Book> book) {
                // Update the cached copy of the words in the adapter.
                adapter.setBooks(book);
            }
        });
    }

    @SuppressWarnings("serial")
    public class TestsServlet extends HttpServlet {
        public void doGet(HttpServletRequest req, HttpServletResponse resp)
                throws IOException {
            try {
                Properties props = new Properties();
                Session session = Session.getDefaultInstance(props, null);

                String message = "Corps du message";

                Message msg = new MimeMessage(session);
                msg.setFrom(new InternetAddress("lordlong59@gmail.com", "L"));
                msg.addRecipient(Message.RecipientType.TO,
                        new InternetAddress("phigreg96@gmail.com", "G"));
                msg.setSubject("Bienvenue sur mon application !");
                msg.setText(message);
                Transport.send(msg);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
    }
}
