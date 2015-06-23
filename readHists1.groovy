 import org.root.group.*;
 import org.root.pad.*;
 import org.root.func.*;
 import org.root.histogram.*;

// code to read in histogram file from readData3.groovy and make screen plots.

// open histogram file.
TDirectory dirFile = new TDirectory();
dirFile.readFile("histFile.0.evio");
//dirFile.ls();

// get the hists out of the file.
H1D hdpp = (H1D) dirFile.getDirectory("electrons").getObject("hdpp");
H1D hpDiff = (H1D) dirFile.getDirectory("electrons").getObject("hpDiff");
H1D hthetaDiff = (H1D) dirFile.getDirectory("electrons").getObject("hthetaDiff");
H1D hphiDiff = (H1D) dirFile.getDirectory("electrons").getObject("hphiDiff");
H1D hpRec = (H1D) dirFile.getDirectory("electrons").getObject("hpRec");
H1D hpGen = (H1D) dirFile.getDirectory("electrons").getObject("hpGen");
H1D hthetaRec = (H1D) dirFile.getDirectory("electrons").getObject("hthetaRec");
H1D hthetaGen = (H1D) dirFile.getDirectory("electrons").getObject("hthetaGen");
H2D hpThetaGen = (H2D) dirFile.getDirectory("electrons").getObject("hpThetaGen");
H2D hpThetaRec = (H2D) dirFile.getDirectory("electrons").getObject("hpThetaRec");
H2D hThetaRecPhiRec = (H2D) dirFile.getDirectory("electrons").getObject("hThetaRecPhiRec");
H2D hdThetaRecPgen = (H2D) dirFile.getDirectory("electrons").getObject("hdThetaRecPgen");
H2D hdThetaRecThetaGen = (H2D) dirFile.getDirectory("electrons").getObject("hdThetaRecThetaGen");
H2D hdThetaRecPhiGen = (H2D) dirFile.getDirectory("electrons").getObject("hdThetaRecPhiGen");

H2D hdpRecPgen = (H2D) dirFile.getDirectory("electrons").getObject("hdpRecPgen");
H2D hdpRecThetaGen = (H2D) dirFile.getDirectory("electrons").getObject("hdpRecThetaGen");
H2D hdpRecPhiGen = (H2D) dirFile.getDirectory("electrons").getObject("hdpRecPhiGen");

H2D hdPhiRecPgen = (H2D) dirFile.getDirectory("electrons").getObject("hdPhiRecPgen");
H2D hdPhiRecThetaGen = (H2D) dirFile.getDirectory("electrons").getObject("hdPhiRecThetaGen");
H2D hdPhiRecPhiGen = (H2D) dirFile.getDirectory("electrons").getObject("hdPhiRecPhiGen");
H2D hdpRecPrec = (H2D)dirFile.getDirectory("electrons").getObject("hdpRecPrec");
H1D hvz = (H1D) dirFile.getDirectory("electrons").getObject("hvz");
H1D hdpTheta = (H1D) dirFile.getDirectory("electrons").getObject("hdpTheta");


// make first canvas. ********************************************
TCanvas c1 = new TCanvas("c1","Physics Analysis 1",800,640,2,2);

hdpp.setLineWidth(2);
hdpp.setLineColor(2);
hdpp.setXTitle("(prec-pgen)/pgen (GeV/c)");
c1.cd(0);
c1.draw(hdpp);

hpDiff.setLineWidth(2);
hpDiff.setLineColor(2);
hpDiff.setXTitle("pDiff (GeV/c)");
c1.cd(1);
c1.draw(hpDiff);

hthetaDiff.setLineWidth(2);
hthetaDiff.setLineColor(2);
hthetaDiff.setXTitle("thetaDiff (deg)");
c1.cd(2);
c1.draw(hthetaDiff);

hphiDiff.setLineWidth(2);
hphiDiff.setLineColor(2);
hphiDiff.setXTitle("phiDiff (deg)");
c1.cd(3);
c1.draw(hphiDiff);

// make second canvas. *******************************************
TCanvas c2 = new TCanvas("c2", "Physics Analysis 2", 800, 640, 2, 2);

// dp
c2.cd(0);
hdpRecPgen.setXTitle("pGen (GeV/c)");
hdpRecPgen.setYTitle("dp (GeV/c)");
c2.draw(hdpRecPgen);

c2.cd(1);
hdpRecThetaGen.setXTitle("thetaGen (deg)");
hdpRecThetaGen.setYTitle("dp (GeV/c)");
c2.draw(hdpRecThetaGen);

c2.cd(2);
hdpRecPhiGen.setXTitle("phiGen (deg)");
hdpRecPhiGen.setYTitle("dp (GeV/c)");
c2.draw(hdpRecPhiGen);

c2.cd(3);
hdpRecPrec.setXTitle("pRec (GeV/c)");
hdpRecPrec.setYTitle("dp (GeV/c)");
c2.draw(hdpRecPrec);


// make third canvas. ********************************************
TCanvas c3 = new TCanvas("c3", "Physics Analysis 3", 800, 640, 2, 2);

// dPhi
c3.cd(0);
hdPhiRecPgen.setXTitle("pGen (GeV/c)");
hdPhiRecPgen.setYTitle("dPhiRec (deg)");
c3.draw(hdPhiRecPgen);

c3.cd(1);
hdPhiRecThetaGen.setXTitle("thetaGen (deg)");
hdPhiRecThetaGen.setYTitle("dPhiRec (deg)");
c3.draw(hdPhiRecThetaGen);

c3.cd(2);
hdPhiRecPhiGen.setXTitle("phiGen (deg)");
hdPhiRecPhiGen.setYTitle("dPhiRec (deg)");
c3.draw(hdPhiRecPhiGen);

hvz.setLineWidth(2);
hvz.setLineColor(2);
c3.cd(3);
hvz.setXTitle("vz (cm)");
c3.draw(hvz);




// make fourth canvas. *******************************************
TCanvas c4 = new TCanvas("c4", "Physics Analysis 4", 800, 640, 2, 2);

// dTheta
c4.cd(0);
hdThetaRecPgen.setXTitle("pGen (GeV/c)");
hdThetaRecPgen.setYTitle("dThetaRec (deg)");
c4.draw(hdThetaRecPgen);

c4.cd(1);
hdThetaRecThetaGen.setXTitle("thetaGen (deg)");
hdThetaRecThetaGen.setYTitle("dThetaRec (deg)");
c4.draw(hdThetaRecThetaGen);

c4.cd(2);
hdThetaRecPhiGen.setXTitle("phiGen (deg)");
hdThetaRecPhiGen.setYTitle("dThetaRec (deg)");
c4.draw(hdThetaRecPhiGen);

c4.cd(3);
hdpTheta.setXTitle("hdpTheta (deg)");
c4.draw(hdpTheta);






