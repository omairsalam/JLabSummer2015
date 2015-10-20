import org.jlab.evio.clas12.*;
import org.jlab.clas.physics.*;
import org.jlab.clas12.physics.*;
import org.root.histogram.*;
import org.root.pad.*;
import org.root.group.*;
import org.root.func.*;
import java.lang.Math;
import java.lang.Object;
import org.jlab.geom.*;
import org.jlab.geom.base.*;
import org.jlab.geom.detector.ec.*;
import org.jlab.clas12.dbdata.DataBaseLoader.*;
import org.jlab.clas12.dbdata.*;
import org.jlab.clasrec.utils.*

// code to read output file from the CLAS12 reconstruction, create and fill hists,
// and save the histogram output. Written for future use on the cluster.
//                                                    - gpg 6/21/15

// open input file here. *******************************************************
EvioSource reader = new EvioSource();
reader.open("gmnElectrons2kRec.0.evio");

// open histogram output file here. ********************************************
TDirectory histFile = new TDirectory();
histFile.mkdir("electrons");

// create a fitter to get event data
GenericKinematicFitter fitter = new GenericKinematicFitter(11.0,"11:X-");

// user quantities.  ***********************************************************
EvioDataEvent event;
PhysicsEvent recEvent;
PhysicsEvent genEvent;
Particle recElectron;
Particle genElectron;

int recElecCount = 0, genElecCount = 0, evCount = 0;
double recPTotal = 0, genPTotal = 0, recEnergy = 0, genEnergy = 0, recQ2 = 0, difference = 0;
double costheta, thetaRec, phiRec, pi=3.141592654;

// stuff for using CLAS12 geometry

ConstantProvider constants = DataBaseLoader.getConstantsEC();
ECDetector detector;
detector = new ECFactory().createDetectorCLAS(constants);
detector = new ECFactory().createDetectorSector(constants);
detector = new ECFactory().createDetectorTilted(constants);
detector = new ECFactory().createDetectorLocal(constants);

ECFactory factory = new ECFactory();

// Constructed in “local” coordinates (varies by detector)
ECSector firstSector = factory.createSector(constants, 0);
//ECSuperlayer outerEC = ECFactory.createSuperlayer(constants, 0, 2);
//ECLayer wLayer = ECFactory.createLayer(constants, 0, 2, 2);

// end of geometry declarations

// Define histograms in the output file. ***********************************

histFile.getDirectory("electrons").add(new H1D("hpRec", 110, 0, 11.0));
histFile.getDirectory("electrons").add(new H1D("hpGen", 110, 0, 11.0));
histFile.getDirectory("electrons").add(new H1D("hpDiff", 100, -1.0, 1.0));
histFile.getDirectory("electrons").add(new H1D("hthetaDiff", 100, -1.0, 1.0));
histFile.getDirectory("electrons").add(new H1D("hphiDiff", 100, -30.0, 10.0));
histFile.getDirectory("electrons").add(new H1D("hthetaRec", 100, 0, 50.0));
histFile.getDirectory("electrons").add(new H1D("hthetaGen", 100, 0, 50.0));
histFile.getDirectory("electrons").add(new H2D("hpThetaGen", 100, 0, 50.0,110,0,11.0));
histFile.getDirectory("electrons").add(new H2D("hpThetaRec", 100, 0, 50.0,110,0,11.0));
histFile.getDirectory("electrons").add(new H2D("hThetaRecPhiRec", 180, -180, 180, 100,0, 50.0));
histFile.getDirectory("electrons").add(new H1D("hdpp", 50, -0.075, 0.075));
histFile.getDirectory("electrons").add(new H2D("hdThetaRecPgen", 110,0,11, 170,-0.5,1.2));
histFile.getDirectory("electrons").add(new H2D("hdThetaRecThetaGen", 100,0,50, 170,-0.5,1.2));
histFile.getDirectory("electrons").add(new H2D("hdThetaRecPhiGen", 180,-180,180, 170,-0.5,1.2));
histFile.getDirectory("electrons").add(new H2D("hdpRecPgen", 110,0,11, 100,-0.05,0.05));
histFile.getDirectory("electrons").add(new H2D("hdpRecThetaGen", 50,0,50, 100,-0.05,0.05));
histFile.getDirectory("electrons").add(new H2D("hdpRecPhiGen", 180, -180, 180, 100,-0.05,0.05));
histFile.getDirectory("electrons").add(new H2D("hdPhiRecPgen", 110,0,11, 45,-15,0));
histFile.getDirectory("electrons").add(new H2D("hdPhiRecThetaGen", 100,0,45, 45,-15,0));
histFile.getDirectory("electrons").add(new H2D("hdPhiRecPhiGen", 180,-180,180, 45,-15,0));
histFile.getDirectory("electrons").add(new H2D("hdpRecPrec", 110,0,11, 100,-0.05,0.05));
histFile.getDirectory("electrons").add(new H1D("hvz", 180, -0.5, 2.5));
histFile.getDirectory("electrons").add(new H1D("hdpTheta", 180, -0.1, 0.1));
histFile.getDirectory("electrons").add(new H1D("hdpPhi", 180, -1, 1));

// Define the same hists locally.  *************************************

H1D hpRec = (H1D) histFile.getDirectory("electrons").getObject("hpRec");
H1D hpGen = (H1D) histFile.getDirectory("electrons").getObject("hpGen");
H1D hpDiff = (H1D) histFile.getDirectory("electrons").getObject("hpDiff");
H1D hthetaDiff = (H1D) histFile.getDirectory("electrons").getObject("hthetaDiff");
H1D hphiDiff = (H1D) histFile.getDirectory("electrons").getObject("hphiDiff");
H1D hthetaRec = (H1D) histFile.getDirectory("electrons").getObject("hthetaRec");
H1D hthetaGen = (H1D) histFile.getDirectory("electrons").getObject("hthetaGen");
H2D hpThetaGen = (H2D) histFile.getDirectory("electrons").getObject("hpThetaGen");
H2D hpThetaRec = (H2D) histFile.getDirectory("electrons").getObject("hpThetaRec");
H2D hThetaRecPhiRec = (H2D) histFile.getDirectory("electrons").getObject("hThetaRecPhiRec");
H1D hdpp = (H1D) histFile.getDirectory("electrons").getObject("hdpp");
H2D hdThetaRecPgen = (H2D) histFile.getDirectory("electrons").getObject("hdThetaRecPgen");
H2D hdThetaRecThetaGen = (H2D) histFile.getDirectory("electrons").getObject("hdThetaRecThetaGen");
H2D hdThetaRecPhiGen = (H2D) histFile.getDirectory("electrons").getObject("hdThetaRecPhiGen");
H2D hdpRecPgen = (H2D) histFile.getDirectory("electrons").getObject("hdpRecPgen");
H2D hdpRecThetaGen = (H2D) histFile.getDirectory("electrons").getObject("hdpRecThetaGen");
H2D hdpRecPhiGen = (H2D) histFile.getDirectory("electrons").getObject("hdpRecPhiGen");
H2D hdPhiRecPgen = (H2D) histFile.getDirectory("electrons").getObject("hdPhiRecPgen");
H2D hdPhiRecThetaGen = (H2D) histFile.getDirectory("electrons").getObject("hdPhiRecThetaGen");
H2D hdPhiRecPhiGen = (H2D) histFile.getDirectory("electrons").getObject("hdPhiRecPhiGen");
H2D hdpRecPrec = (H2D) histFile.getDirectory("electrons").getObject("hdpRecPrec");
H1D hvz = (H1D) histFile.getDirectory("electrons").getObject("hvz");
H1D hdpTheta = (H1D) histFile.getDirectory("electrons").getObject("hdpTheta");
H1D hdpPhi = (H1D) histFile.getDirectory("electrons").getObject("hdpPhi");

// loop over the data here. *******************************************************************
while(reader.hasEvent()){
   // get the events
   event = reader.getNextEvent();
   recEvent = fitter.getPhysicsEvent(event);
   genEvent = fitter.getGeneratedEvent(event);

   // determine how many electrons where detected in event
   //recElecCount = recEvent.countByPid(11);
   //genElecCount = genEvent.countByPid(11);
   recElecCount = recEvent.countByCharge(-1);
   genElecCount = genEvent.countByCharge(-1);
   //System.out.println("recElecCount="+recElecCount);


    // if any electrons were detected then loop over them
    if(recElecCount > 0) {
        // loop over tracks in the event.
        for(int i = 0; i < recElecCount; i++) { 
	    // count up the number of electrons/negative tracks
            //recElectron = recEvent.getParticleByPid(11, i);
            //genElectron = genEvent.getParticleByPid(11, i);
            recElectron = recEvent.getParticleByCharge(-1, i);
            genElectron = genEvent.getParticleByCharge(-1, i);

	    // get total momentum and fill hists.
            recPTotal = Math.sqrt((recElectron.px() * recElectron.px()) + (recElectron.py() * recElectron.py()) + (recElectron.pz() * recElectron.pz()));
            if (genElectron != null) {
	       genPTotal = Math.sqrt((genElectron.px() * genElectron.px()) + (genElectron.py() * genElectron.py()) + (genElectron.pz() * genElectron.pz()));
	    } else {
	       genPTotal = 0;
	    }
	    hpRec.fill(recPTotal);
	    hpGen.fill(genPTotal);
            difference = recPTotal - genPTotal;
	    double presol = difference/genPTotal; //the resolution
            //System.out.println("pdiff= " + difference);

	    // get angles and fill hists.
	    costheta=recElectron.pz()/Math.sqrt(Math.pow(recElectron.px(),2.0) + Math.pow(recElectron.py(),2.0) + Math.pow(recElectron.pz(),2.0));
	    thetaRec=Math.acos(costheta)*180/pi;
	    //System.out.println("costheta="+costheta+" theta="+thetaRec);
	    hthetaRec.fill(thetaRec);

	    phiRec=Math.atan2(recElectron.py(),recElectron.px())*180/pi;

            if (genElectron != null) {
	       costheta=genElectron.pz()/Math.sqrt(Math.pow(genElectron.px(),2.0) + Math.pow(genElectron.py(),2.0) + Math.pow(genElectron.pz(),2.0));
	       thetaGen=Math.acos(costheta)*180/pi;
	       hthetaGen.fill(thetaGen);
	       phiGen=Math.atan2(genElectron.py(),genElectron.px())*180/pi;
	    } else {
	       costheta=0;
	       thetaGen=Math.acos(costheta)*180/pi;
	       phiGen=0
	    }  

	    thetaDiff = thetaRec - thetaGen;
	    double resTheta = thetaDiff/thetaGen;
	    phiDiff = phiRec - phiGen;
	    double resPhi = phiDiff/phiGen; 

	    hThetaRecPhiRec.fill(phiRec,thetaRec);

	    hpThetaGen.fill(thetaGen,genPTotal);
	    hpThetaRec.fill(thetaRec,recPTotal);
	    //System.out.println(thetaRec+" "+recPTotal);

	    hpDiff.fill(difference);
	    hdpp.fill(presol);
	    hdpRecPgen.fill(genPTotal,presol);
	    hdpRecThetaGen.fill(thetaGen,presol);
	    hdpRecPhiGen.fill(phiGen,presol);

	    hthetaDiff.fill(thetaDiff);
	    hdThetaRecPgen.fill(genPTotal,thetaDiff);
	    hdThetaRecThetaGen.fill(thetaGen,thetaDiff);
	    hdThetaRecPhiGen.fill(phiGen,thetaDiff);

	    hphiDiff.fill(phiDiff);
	    hdPhiRecPgen.fill(genPTotal,phiDiff);
	    hdPhiRecThetaGen.fill(thetaGen,phiDiff);
	    hdPhiRecPhiGen.fill(phiGen,phiDiff);
	    hdpRecPrec.fill(recPTotal,presol);
	    hvz.fill(recElectron.vertex().z());
	    hdpTheta.fill(resTheta);
	    hdpPhi.fill(resPhi);

        } // end for loop over tracks
    } // end if to check on electrons in the event.

   //event.show(); // print out all banks in the event
} // end of loop over events.

// write it to the output file.
histFile.write("histFile.evio");
