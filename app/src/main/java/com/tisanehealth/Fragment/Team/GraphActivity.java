package com.tisanehealth.Fragment.Team;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.google.gson.Gson;
import com.tisanehealth.Activity.DashBoardActivity;
import com.tisanehealth.Helper.CustomLoader;
import com.tisanehealth.Helper.Preferences;
import com.tisanehealth.Model.tree.TreeL1P1;
import com.tisanehealth.Model.tree.TreeL2P1;
import com.tisanehealth.Model.tree.TreeL2P2;
import com.tisanehealth.Model.tree.TreeL3P1;
import com.tisanehealth.Model.tree.TreeL3P2;
import com.tisanehealth.Model.tree.TreeL3P3;
import com.tisanehealth.Model.tree.TreeL3P4;
import com.tisanehealth.Model.tree.TreeL4P1;
import com.tisanehealth.Model.tree.TreeL4P2;
import com.tisanehealth.Model.tree.TreeL4P3;
import com.tisanehealth.Model.tree.TreeL4P4;
import com.tisanehealth.Model.tree.TreeL4P5;
import com.tisanehealth.Model.tree.TreeL4P6;
import com.tisanehealth.Model.tree.TreeL4P7;
import com.tisanehealth.Model.tree.TreeL4P8;
import com.tisanehealth.Model.tree.TreeResponse;
import com.tisanehealth.R;

import org.json.JSONException;
import org.json.JSONObject;

import de.blox.graphview.BaseGraphAdapter;
import de.blox.graphview.Graph;
import de.blox.graphview.GraphAdapter;
import de.blox.graphview.GraphView;
import de.blox.graphview.Node;
import de.blox.graphview.ViewHolder;

import static com.tisanehealth.Helper.AppUrls.BaseUrl;
import static com.tisanehealth.Helper.AppUrls.BindDashBoradTreeview;

public abstract class GraphActivity extends AppCompatActivity {
    private int nodeCount = 1;
    private Node currentNode;
    protected BaseGraphAdapter<ViewHolder> adapter;
    CustomLoader loader;
    Preferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        loader = new CustomLoader(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        pref = new Preferences(this);

        findViewById(R.id.rlBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.btnGet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!((EditText) findViewById(R.id.memberId)).getText().toString().equals("")) {
                    getData((((EditText) findViewById(R.id.memberId)).getText()).toString());
                } else {
                    Toast.makeText(GraphActivity.this, "Enter Member Id", Toast.LENGTH_SHORT).show();
                }
            }
        });


        //getData();
        initViews(DashBoardActivity.treeResponse);
    }


    private void initViews(TreeResponse treeResponse) {
        ((TextView) findViewById(R.id.teamaAName)).setText(treeResponse.getTreeDashboard().get(0).getTeamA());
        ((TextView) findViewById(R.id.teamaAId)).setText(treeResponse.getTreeDashboard().get(0).getLeftMember());
        ((TextView) findViewById(R.id.teamaAActive)).setText(treeResponse.getTreeDashboard().get(0).getLInactiveMember());
        ((TextView) findViewById(R.id.teamaABP)).setText(treeResponse.getTreeDashboard().get(0).getLpv());
        ((TextView) findViewById(R.id.teamaBName)).setText(treeResponse.getTreeDashboard().get(0).getTeamB());
        ((TextView) findViewById(R.id.teamBId)).setText(treeResponse.getTreeDashboard().get(0).getRightMember());
        ((TextView) findViewById(R.id.teamaBActive)).setText(treeResponse.getTreeDashboard().get(0).getRInactiveMember());
        ((TextView) findViewById(R.id.teamaBBP)).setText(treeResponse.getTreeDashboard().get(0).getRpv());

        final Graph graph = createGraph(treeResponse);
        setupAdapter(graph);
    }


    private void setupAdapter(Graph graph) {
        final GraphView graphView = findViewById(R.id.graph);

        adapter = new BaseGraphAdapter<ViewHolder>(graph) {

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.node, parent, false);
                return new SimpleViewHolder(view);
            }

            @Override
            public void onBindViewHolder(ViewHolder viewHolder, Object data, int position) {
                if (data instanceof TreeL1P1) {
                    TreeL1P1 model = (TreeL1P1) data;
                    ((SimpleViewHolder) viewHolder).textView.setText(model.getMemberAppCode() + "\n" + model.getMemberName());
                } else if (data instanceof TreeL2P1) {
                    TreeL2P1 model = (TreeL2P1) data;
                    ((SimpleViewHolder) viewHolder).textView.setText(model.getMemberAppCode() + "\n" + model.getMemberName());

                } else if (data instanceof TreeL2P2) {
                    TreeL2P2 model = (TreeL2P2) data;
                    ((SimpleViewHolder) viewHolder).textView.setText(model.getMemberAppCode() + "\n" + model.getMemberName());

                } else if (data instanceof TreeL3P1) {
                    TreeL3P1 model = (TreeL3P1) data;
                    ((SimpleViewHolder) viewHolder).textView.setText(model.getMemberAppCode() + "\n" + model.getMemberName());

                } else if (data instanceof TreeL3P2) {
                    TreeL3P2 model = (TreeL3P2) data;
                    ((SimpleViewHolder) viewHolder).textView.setText(model.getMemberAppCode() + "\n" + model.getMemberName());

                } else if (data instanceof TreeL3P3) {
                    TreeL3P3 model = (TreeL3P3) data;
                    ((SimpleViewHolder) viewHolder).textView.setText(model.getMemberAppCode() + "\n" + model.getMemberName());

                } else if (data instanceof TreeL3P4) {
                    TreeL3P4 model = (TreeL3P4) data;
                    ((SimpleViewHolder) viewHolder).textView.setText(model.getMemberAppCode() + "\n" + model.getMemberName());

                } else if (data instanceof TreeL4P1) {
                    TreeL4P1 model = (TreeL4P1) data;
                    ((SimpleViewHolder) viewHolder).textView.setText(model.getMemberAppCode() + "\n" + model.getMemberName());

                } else if (data instanceof TreeL4P2) {
                    TreeL4P2 model = (TreeL4P2) data;
                    ((SimpleViewHolder) viewHolder).textView.setText(model.getMemberAppCode() + "\n" + model.getMemberName());

                } else if (data instanceof TreeL4P3) {
                    TreeL4P3 model = (TreeL4P3) data;
                    ((SimpleViewHolder) viewHolder).textView.setText(model.getMemberAppCode() + "\n" + model.getMemberName());

                } else if (data instanceof TreeL4P4) {
                    TreeL4P4 model = (TreeL4P4) data;
                    ((SimpleViewHolder) viewHolder).textView.setText(model.getMemberAppCode() + "\n" + model.getMemberName());

                } else if (data instanceof TreeL4P5) {
                    TreeL4P5 model = (TreeL4P5) data;
                    ((SimpleViewHolder) viewHolder).textView.setText(model.getMemberAppCode() + "\n" + model.getMemberName());

                } else if (data instanceof TreeL4P6) {
                    TreeL4P6 model = (TreeL4P6) data;
                    ((SimpleViewHolder) viewHolder).textView.setText(model.getMemberAppCode() + "\n" + model.getMemberName());

                } else if (data instanceof TreeL4P7) {
                    TreeL4P7 model = (TreeL4P7) data;
                    ((SimpleViewHolder) viewHolder).textView.setText(model.getMemberAppCode() + "\n" + model.getMemberName());

                } else if (data instanceof TreeL4P8) {
                    TreeL4P8 model = (TreeL4P8) data;
                    ((SimpleViewHolder) viewHolder).textView.setText(model.getMemberAppCode() + "\n" + model.getMemberName());

                }

            }

            class SimpleViewHolder extends ViewHolder {
                TextView textView;

                SimpleViewHolder(View itemView) {
                    super(itemView);
                    textView = itemView.findViewById(R.id.textView);
                }
            }
        }

        ;

        setAlgorithm(adapter);

        graphView.setAdapter(adapter);
        graphView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressWarnings("StringConcatenationInsideStringBufferAppend")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentNode = adapter.getNode(position);
                StringBuffer stringBuffer = new StringBuffer();
                if (currentNode.getData() instanceof TreeL1P1) {
                    TreeL1P1 model = (TreeL1P1) currentNode.getData();
                    stringBuffer.append("Package Name:   " + model.getPackageName() + "\n");
                    stringBuffer.append("BP:   " + model.getPV() + "\n");
                    stringBuffer.append("LBP:   " + model.getLPV() + "\n");
                    stringBuffer.append("RBP:   " + model.getRPV() + "\n");
                    stringBuffer.append("Registration Date  : " + model.getRegDate() + "\n");
                    stringBuffer.append("Activation Date:   " + model.getActivationDate() + "\n");
                    stringBuffer.append("Sponsor Id:   " + model.getSponsorId() + "\n");
                    stringBuffer.append("Sponsor Name:   " + model.getRidname() + "\n");
                    stringBuffer.append("Imm. Sponsor Id:   " + model.getReferralId() + "\n");
                    stringBuffer.append("Imm. Sponsor Name:   " + model.getPidname() + "\n");
                } else if (currentNode.getData() instanceof TreeL2P1) {
                    TreeL2P1 model = (TreeL2P1) currentNode.getData();
                    stringBuffer.append("Package Name:   " + model.getPackageName() + "\n");
                    stringBuffer.append("BP:   " + model.getPV() + "\n");
                    stringBuffer.append("LBP:   " + model.getLPV() + "\n");
                    stringBuffer.append("RBP:   " + model.getRPV() + "\n");
                    stringBuffer.append("Registration Date  : " + model.getRegDate() + "\n");
                    stringBuffer.append("Activation Date:   " + model.getActivationDate() + "\n");
                    stringBuffer.append("Sponsor Id:   " + model.getSponsorId() + "\n");
                    stringBuffer.append("Sponsor Name:   " + model.getRidname() + "\n");
                    stringBuffer.append("Imm. Sponsor Id:   " + model.getReferralId() + "\n");
                    stringBuffer.append("Imm. Sponsor Name:   " + model.getPidname() + "\n");
                } else if (currentNode.getData() instanceof TreeL2P2) {
                    TreeL2P2 model = (TreeL2P2) currentNode.getData();
                    stringBuffer.append("Package Name:   " + model.getPackageName() + "\n");
                    stringBuffer.append("BP:   " + model.getPV() + "\n");
                    stringBuffer.append("LBP:   " + model.getLPV() + "\n");
                    stringBuffer.append("RBP:   " + model.getRPV() + "\n");
                    stringBuffer.append("Registration Date  : " + model.getRegDate() + "\n");
                    stringBuffer.append("Activation Date:   " + model.getActivationDate() + "\n");
                    stringBuffer.append("Sponsor Id:   " + model.getSponsorId() + "\n");
                    stringBuffer.append("Sponsor Name:   " + model.getRidname() + "\n");
                    stringBuffer.append("Imm. Sponsor Id:   " + model.getReferralId() + "\n");
                    stringBuffer.append("Imm. Sponsor Name:   " + model.getPidname() + "\n");
                } else if (currentNode.getData() instanceof TreeL3P1) {
                    TreeL3P1 model = (TreeL3P1) currentNode.getData();
                    stringBuffer.append("Package Name:   " + model.getPackageName() + "\n");
                    stringBuffer.append("BP:   " + model.getPV() + "\n");
                    stringBuffer.append("LBP:   " + model.getLPV() + "\n");
                    stringBuffer.append("RBP:   " + model.getRPV() + "\n");
                    stringBuffer.append("Registration Date  : " + model.getRegDate() + "\n");
                    stringBuffer.append("Activation Date:   " + model.getActivationDate() + "\n");
                    stringBuffer.append("Sponsor Id:   " + model.getSponsorId() + "\n");
                    stringBuffer.append("Sponsor Name:   " + model.getRidname() + "\n");
                    stringBuffer.append("Imm. Sponsor Id:   " + model.getReferralId() + "\n");
                    stringBuffer.append("Imm. Sponsor Name:   " + model.getPidname() + "\n");
                } else if (currentNode.getData() instanceof TreeL3P2) {
                    TreeL3P2 model = (TreeL3P2) currentNode.getData();
                    stringBuffer.append("Package Name:   " + model.getPackageName() + "\n");
                    stringBuffer.append("BP:   " + model.getPV() + "\n");
                    stringBuffer.append("LBP:   " + model.getLPV() + "\n");
                    stringBuffer.append("RBP:   " + model.getRPV() + "\n");
                    stringBuffer.append("Registration Date  : " + model.getRegDate() + "\n");
                    stringBuffer.append("Activation Date:   " + model.getActivationDate() + "\n");
                    stringBuffer.append("Sponsor Id:   " + model.getSponsorId() + "\n");
                    stringBuffer.append("Sponsor Name:   " + model.getRidname() + "\n");
                    stringBuffer.append("Imm. Sponsor Id:   " + model.getReferralId() + "\n");
                    stringBuffer.append("Imm. Sponsor Name:   " + model.getPidname() + "\n");
                } else if (currentNode.getData() instanceof TreeL3P3) {
                    TreeL3P3 model = (TreeL3P3) currentNode.getData();
                    stringBuffer.append("Package Name:   " + model.getPackageName() + "\n");
                    stringBuffer.append("BP:   " + model.getPV() + "\n");
                    stringBuffer.append("LBP:   " + model.getLPV() + "\n");
                    stringBuffer.append("RBP:   " + model.getRPV() + "\n");
                    stringBuffer.append("Registration Date  : " + model.getRegDate() + "\n");
                    stringBuffer.append("Activation Date:   " + model.getActivationDate() + "\n");
                    stringBuffer.append("Sponsor Id:   " + model.getSponsorId() + "\n");
                    stringBuffer.append("Sponsor Name:   " + model.getRidname() + "\n");
                    stringBuffer.append("Imm. Sponsor Id:   " + model.getReferralId() + "\n");
                    stringBuffer.append("Imm. Sponsor Name:   " + model.getPidname() + "\n");
                } else if (currentNode.getData() instanceof TreeL3P4) {
                    TreeL3P4 model = (TreeL3P4) currentNode.getData();
                    stringBuffer.append("Package Name:   " + model.getPackageName() + "\n");
                    stringBuffer.append("BP:   " + model.getPV() + "\n");
                    stringBuffer.append("LBP:   " + model.getLPV() + "\n");
                    stringBuffer.append("RBP:   " + model.getRPV() + "\n");
                    stringBuffer.append("Registration Date  : " + model.getRegDate() + "\n");
                    stringBuffer.append("Activation Date:   " + model.getActivationDate() + "\n");
                    stringBuffer.append("Sponsor Id:   " + model.getSponsorId() + "\n");
                    stringBuffer.append("Sponsor Name:   " + model.getRidname() + "\n");
                    stringBuffer.append("Imm. Sponsor Id:   " + model.getReferralId() + "\n");
                    stringBuffer.append("Imm. Sponsor Name:   " + model.getPidname() + "\n");
                } else if (currentNode.getData() instanceof TreeL4P1) {
                    TreeL4P1 model = (TreeL4P1) currentNode.getData();
                    stringBuffer.append("Package Name:   " + model.getPackageName() + "\n");
                    stringBuffer.append("BP:   " + model.getPV() + "\n");
                    stringBuffer.append("LBP:   " + model.getLPV() + "\n");
                    stringBuffer.append("RBP:   " + model.getRPV() + "\n");
                    stringBuffer.append("Registration Date  : " + model.getRegDate() + "\n");
                    stringBuffer.append("Activation Date:   " + model.getActivationDate() + "\n");
                    stringBuffer.append("Sponsor Id:   " + model.getSponsorId() + "\n");
                    stringBuffer.append("Sponsor Name:   " + model.getRidname() + "\n");
                    stringBuffer.append("Imm. Sponsor Id:   " + model.getReferralId() + "\n");
                    stringBuffer.append("Imm. Sponsor Name:   " + model.getPidname() + "\n");
                } else if (currentNode.getData() instanceof TreeL4P2) {
                    TreeL4P2 model = (TreeL4P2) currentNode.getData();
                    stringBuffer.append("Package Name:   " + model.getPackageName() + "\n");
                    stringBuffer.append("BP:   " + model.getPV() + "\n");
                    stringBuffer.append("LBP:   " + model.getLPV() + "\n");
                    stringBuffer.append("RBP:   " + model.getRPV() + "\n");
                    stringBuffer.append("Registration Date  : " + model.getRegDate() + "\n");
                    stringBuffer.append("Activation Date:   " + model.getActivationDate() + "\n");
                    stringBuffer.append("Sponsor Id:   " + model.getSponsorId() + "\n");
                    stringBuffer.append("Sponsor Name:   " + model.getRidname() + "\n");
                    stringBuffer.append("Imm. Sponsor Id:   " + model.getReferralId() + "\n");
                    stringBuffer.append("Imm. Sponsor Name:   " + model.getPidname() + "\n");
                } else if (currentNode.getData() instanceof TreeL4P3) {
                    TreeL4P3 model = (TreeL4P3) currentNode.getData();
                    stringBuffer.append("Package Name:   " + model.getPackageName() + "\n");
                    stringBuffer.append("BP:   " + model.getPV() + "\n");
                    stringBuffer.append("LBP:   " + model.getLPV() + "\n");
                    stringBuffer.append("RBP:   " + model.getRPV() + "\n");
                    stringBuffer.append("Registration Date  : " + model.getRegDate() + "\n");
                    stringBuffer.append("Activation Date:   " + model.getActivationDate() + "\n");
                    stringBuffer.append("Sponsor Id:   " + model.getSponsorId() + "\n");
                    stringBuffer.append("Sponsor Name:   " + model.getRidname() + "\n");
                    stringBuffer.append("Imm. Sponsor Id:   " + model.getReferralId() + "\n");
                    stringBuffer.append("Imm. Sponsor Name:   " + model.getPidname() + "\n");
                } else if (currentNode.getData() instanceof TreeL4P4) {
                    TreeL4P4 model = (TreeL4P4) currentNode.getData();
                    stringBuffer.append("Package Name:   " + model.getPackageName() + "\n");
                    stringBuffer.append("BP:   " + model.getPV() + "\n");
                    stringBuffer.append("LBP:   " + model.getLPV() + "\n");
                    stringBuffer.append("RBP:   " + model.getRPV() + "\n");
                    stringBuffer.append("Registration Date  : " + model.getRegDate() + "\n");
                    stringBuffer.append("Activation Date:   " + model.getActivationDate() + "\n");
                    stringBuffer.append("Sponsor Id:   " + model.getSponsorId() + "\n");
                    stringBuffer.append("Sponsor Name:   " + model.getRidname() + "\n");
                    stringBuffer.append("Imm. Sponsor Id:   " + model.getReferralId() + "\n");
                    stringBuffer.append("Imm. Sponsor Name:   " + model.getPidname() + "\n");
                } else if (currentNode.getData() instanceof TreeL4P5) {
                    TreeL4P5 model = (TreeL4P5) currentNode.getData();
                    stringBuffer.append("Package Name:   " + model.getPackageName() + "\n");
                    stringBuffer.append("BP:   " + model.getPV() + "\n");
                    stringBuffer.append("LBP:   " + model.getLPV() + "\n");
                    stringBuffer.append("RBP:   " + model.getRPV() + "\n");
                    stringBuffer.append("Registration Date  : " + model.getRegDate() + "\n");
                    stringBuffer.append("Activation Date:   " + model.getActivationDate() + "\n");
                    stringBuffer.append("Sponsor Id:   " + model.getSponsorId() + "\n");
                    stringBuffer.append("Sponsor Name:   " + model.getRidname() + "\n");
                    stringBuffer.append("Imm. Sponsor Id:   " + model.getReferralId() + "\n");
                    stringBuffer.append("Imm. Sponsor Name:   " + model.getPidname() + "\n");
                } else if (currentNode.getData() instanceof TreeL4P6) {
                    TreeL4P6 model = (TreeL4P6) currentNode.getData();
                    stringBuffer.append("Package Name:   " + model.getPackageName() + "\n");
                    stringBuffer.append("BP:   " + model.getPV() + "\n");
                    stringBuffer.append("LBP:   " + model.getLPV() + "\n");
                    stringBuffer.append("RBP:   " + model.getRPV() + "\n");
                    stringBuffer.append("Registration Date  : " + model.getRegDate() + "\n");
                    stringBuffer.append("Activation Date:   " + model.getActivationDate() + "\n");
                    stringBuffer.append("Sponsor Id:   " + model.getSponsorId() + "\n");
                    stringBuffer.append("Sponsor Name:   " + model.getRidname() + "\n");
                    stringBuffer.append("Imm. Sponsor Id:   " + model.getReferralId() + "\n");
                    stringBuffer.append("Imm. Sponsor Name:   " + model.getPidname() + "\n");
                } else if (currentNode.getData() instanceof TreeL4P7) {
                    TreeL4P7 model = (TreeL4P7) currentNode.getData();
                    stringBuffer.append("Package Name:   " + model.getPackageName() + "\n");
                    stringBuffer.append("BP:   " + model.getPV() + "\n");
                    stringBuffer.append("LBP:   " + model.getLPV() + "\n");
                    stringBuffer.append("RBP:   " + model.getRPV() + "\n");
                    stringBuffer.append("Registration Date  : " + model.getRegDate() + "\n");
                    stringBuffer.append("Activation Date:   " + model.getActivationDate() + "\n");
                    stringBuffer.append("Sponsor Id:   " + model.getSponsorId() + "\n");
                    stringBuffer.append("Sponsor Name:   " + model.getRidname() + "\n");
                    stringBuffer.append("Imm. Sponsor Id:   " + model.getReferralId() + "\n");
                    stringBuffer.append("Imm. Sponsor Name:   " + model.getPidname() + "\n");
                } else if (currentNode.getData() instanceof TreeL4P8) {
                    TreeL4P8 model = (TreeL4P8) currentNode.getData();
                    stringBuffer.append("Package Name:   " + model.getPackageName() + "\n");
                    stringBuffer.append("BP:   " + model.getPV() + "\n");
                    stringBuffer.append("LBP:   " + model.getLPV() + "\n");
                    stringBuffer.append("RBP:   " + model.getRPV() + "\n");
                    stringBuffer.append("Registration Date  : " + model.getRegDate() + "\n");
                    stringBuffer.append("Activation Date:   " + model.getActivationDate() + "\n");
                    stringBuffer.append("Sponsor Id:   " + model.getSponsorId() + "\n");
                    stringBuffer.append("Sponsor Name:   " + model.getRidname() + "\n");
                    stringBuffer.append("Imm. Sponsor Id:   " + model.getReferralId() + "\n");
                    stringBuffer.append("Imm. Sponsor Name:   " + model.getPidname() + "\n");
                }
                showDialog(stringBuffer.toString());
            }
        });
    }

    private void showDialog(String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setMessage(message);
        alertDialog.setTitle("Info");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Dismiss",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public abstract Graph createGraph(TreeResponse treeResponse);

    public abstract void setAlgorithm(GraphAdapter adapter);

    protected String getNodeText() {
        return "Node " + nodeCount++;
    }

    public void getData(String memberId) {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();
//        loader.show();
//        loader.setCanceledOnTouchOutside(true);
//        loader.setCancelable(false);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("MemberId", memberId);
            jsonObject.put("UserName", memberId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(BaseUrl + BindDashBoradTreeview)
                .addJSONObjectBody(jsonObject)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            TreeResponse treeResponse = new Gson().fromJson(response, TreeResponse.class);
                            initViews(treeResponse);
                        } catch (Exception e) {
                            Toast.makeText(GraphActivity.this, "No Data Found", Toast.LENGTH_SHORT).show();
                        }
                    }


                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Toast.makeText(GraphActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                    }

                });
    }
}
