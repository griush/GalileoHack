import peasy.*;

boolean done = false;

double[] p1 = { 0, 0, 0, 1 };
double[] p2 = { 2, 0, 0, 1.5 };
double[] p3 = { 0, 1, 0, 1 };
double[] p4 = { 0.5, 0, -0.5, 0.54 };

ArrayList<float[]> vectors = new ArrayList<float[]>();

ArrayList<float[]> path = new ArrayList<float[]>();

double[] v = { 1, 2, 0 };

double v1, v2, v3, v4;

double[] v_max = new double[3];

double r = 0.05;

PeasyCam cam;


void setup() {
  cam = new PeasyCam(this, 500);
  fullScreen(P3D);
  v1 = Math.pow((v[0] - p1[0]), 2) + Math.pow((v[1] - p1[1]), 2) + Math.pow((v[2] - p1[2]), 2)
    - Math.pow(p1[3], 2);
  v2 = Math.pow((v[0] - p2[0]), 2) + Math.pow((v[1] - p2[1]), 2) + Math.pow((v[2] - p2[2]), 2)
    - Math.pow(p2[3], 2);
  v3 = Math.pow((v[0] - p3[0]), 2) + Math.pow((v[1] - p3[1]), 2) + Math.pow((v[2] - p3[2]), 2)
    - Math.pow(p3[3], 2);
  v4 = Math.pow((v[0] - p4[0]), 2) + Math.pow((v[1] - p4[1]), 2) + Math.pow((v[2] - p4[2]), 2)
    - Math.pow(p4[3], 2);

  System.out.println("Dev:");
  System.out.println("1: " + v1);
  System.out.println("2: " + v2);
  System.out.println("3: " + v3);
  System.out.println("4: " + v4);
}
float scl = 100;
void drawSphere(double[] args) {
  translate((float)args[0]*scl, (float)args[1]*scl, (float)args[2]*scl);
  sphere((float)args[3]*scl);
  translate(-(float)args[0]*scl, -(float)args[1]*scl, -(float)args[2]*scl);
}

void draw() {
  background(255);
  stroke(0);
    iterate();
  fill(0, 100);
  drawSphere(p1);
  fill(255, 0, 0, 100);
  drawSphere(p2);
  fill(255, 255, 0, 100);
  drawSphere(p3);
  fill(255, 0, 255, 100);
  drawSphere(p4);
  
  push();
  noFill();
  strokeWeight(10);
  beginShape();
  for(int i = 0; i<path.size(); i++) {
    float[] vs = path.get(i);
    vertex(vs[0]*scl,vs[1]*scl,vs[2]*scl);
  }
  endShape();
  pop();
}

void iterate() {
  if(done) return;
  double s_max = Math.abs(v1) + Math.abs(v2) + Math.abs(v3) + Math.abs(v4);
  for (double theta = -Math.PI; theta <= Math.PI; theta += 0.01)
    for (double phi = -Math.PI / 2; phi <= Math.PI / 2; phi += 0.01) {
      double dx = r * Math.sin(theta) * Math.cos(phi);
      double dy = r * Math.sin(theta) * Math.sin(phi);
      double dz = r * Math.sin(phi);

      double[] u = { v[0] + dx, v[1] + dy, v[2] + dz };

      v1 = Math.pow((u[0] - p1[0]), 2) + Math.pow((u[1] - p1[1]), 2) + Math.pow((u[2] - p1[2]), 2)
        - Math.pow(p1[3], 2);
      v2 = Math.pow((u[0] - p2[0]), 2) + Math.pow((u[1] - p2[1]), 2) + Math.pow((u[2] - p2[2]), 2)
        - Math.pow(p2[3], 2);
      v3 = Math.pow((u[0] - p3[0]), 2) + Math.pow((u[1] - p3[1]), 2) + Math.pow((u[2] - p3[2]), 2)
        - Math.pow(p3[3], 2);
      v4 = Math.pow((u[0] - p4[0]), 2) + Math.pow((u[1] - p4[1]), 2) + Math.pow((u[2] - p4[2]), 2)
        - Math.pow(p4[3], 2);

      double sm = Math.abs(v1) + Math.abs(v2) + Math.abs(v3) + Math.abs(v4);
      if (sm < s_max) {
        v_max = u;
        s_max = sm;
      }
    }
    
    if(v_max == v) {
      done = true;
      println("DONE");
    }

  double[] u = v_max;
  v = u;

  v1 = Math.pow((u[0] - p1[0]), 2) + Math.pow((u[1] - p1[1]), 2) + Math.pow((u[2] - p1[2]), 2)
    - Math.pow(p1[3], 2);
  v2 = Math.pow((u[0] - p2[0]), 2) + Math.pow((u[1] - p2[1]), 2) + Math.pow((u[2] - p2[2]), 2)
    - Math.pow(p2[3], 2);
  v3 = Math.pow((u[0] - p3[0]), 2) + Math.pow((u[1] - p3[1]), 2) + Math.pow((u[2] - p3[2]), 2)
    - Math.pow(p3[3], 2);
  v4 = Math.pow((u[0] - p4[0]), 2) + Math.pow((u[1] - p4[1]), 2) + Math.pow((u[2] - p4[2]), 2)
    - Math.pow(p4[3], 2);

  path.add(new float[]{(float)v[0],(float)v[1],(float)v[2]});

  double[] args = {v[0],v[1],v[2],0.1};
  fill(0);
  drawSphere(args);
  
  System.out.printf("Dev: %.2f %.2f %.2f\n", v[0], v[1], v[2]);
  //System.out.println("1: " + v1);
  //System.out.println("2: " + v2);
  //System.out.println("3: " + v3);
  //System.out.println("4: " + v4);
}
