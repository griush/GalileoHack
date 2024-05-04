final double[] p1 = { 0.5, 0.5, 1 };
final double[] p2 = { 2, 0, 1.5 };
final double[] p3 = { 1, 1.68, 2 };

double[] v = { 1, 2 };

final double r = 0.1;
double[] v_max = new double[3];

double v1;
double v2;
double v3;

PImage g;

float huristic(float x, float y){
  float sum = 0;
  sum += Math.abs(Math.pow((x - p1[0]), 2) + Math.pow((y - p1[1]), 2) - Math.pow(p1[2], 2));
  sum += Math.abs(Math.pow((x - p2[0]), 2) + Math.pow((y - p2[1]), 2) - Math.pow(p2[2], 2));
  sum += Math.abs(Math.pow((x - p3[0]), 2) + Math.pow((y - p3[1]), 2) - Math.pow(p3[2], 2));
  return sum;
}

void setup() {
  v1 = Math.pow((v[0] - p1[0]), 2) + Math.pow((v[1] - p1[1]), 2) - Math.pow(p1[2], 2);
  v2 = Math.pow((v[0] - p2[0]), 2) + Math.pow((v[1] - p2[1]), 2) - Math.pow(p2[2], 2);
  v3 = Math.pow((v[0] - p3[0]), 2) + Math.pow((v[1] - p3[1]), 2) - Math.pow(p3[2], 2);

  System.out.println("Dev:");
  System.out.println("1: " + v1);
  System.out.println("2: " + v2);
  System.out.println("3: " + v3);

  size(800, 800);

  g = createImage(800,800,RGB);

}

void draw() {
  g.loadPixels();
  push();
  colorMode(HSB,360,1,1);
  for(int i = 0; i<g.pixels.length; i++) {
    float y = (i / g.width - height/2) / 80f;
    float x = (i % g.width - width/2) / 80f;
    float t = huristic(x,y);
    //println(x,y);
    
    g.pixels[i] = color(map(t,0,10,0,360)%360,1,1);
  }
  pop();
  g.updatePixels();
    image(g,0,0);
    //background(255);
   
     p2[0] = (mouseX - width/2) / 80f;
    p2[1] = (mouseY - height/2) / 80f;

  translate(width/2, height/2);
  scale(80);
  noFill();
  stroke(0);
  strokeWeight(0.05);
  ellipse((float)p1[0], (float)p1[1], 2*(float)p1[2], 2*(float)p1[2]);
  ellipse((float)p2[0], (float)p2[1], 2*(float)p2[2], 2*(float)p2[2]);
  ellipse((float)p3[0], (float)p3[1], 2*(float)p3[2], 2*(float)p3[2]);

  strokeWeight(0.05);

  point((float)p1[0], (float)p1[1]);
  point((float)p2[0], (float)p2[1]);
  point((float)p3[0], (float)p3[1]);
  
  double s_max = Math.abs(v1) + Math.abs(v2) + Math.abs(v3);
  for (double theta = -Math.PI; theta <= Math.PI; theta += 0.01) {
    double dx = r * Math.cos(theta);
    double dy = r * Math.sin(theta);

    double[] u = { v[0] + dx, v[1] + dy};

    v1 = Math.pow((u[0] - p1[0]), 2) + Math.pow((u[1] - p1[1]), 2) - Math.pow(p1[2], 2);
    v2 = Math.pow((u[0] - p2[0]), 2) + Math.pow((u[1] - p2[1]), 2) - Math.pow(p2[2], 2);
    v3 = Math.pow((u[0] - p3[0]), 2) + Math.pow((u[1] - p3[1]), 2) - Math.pow(p3[2], 2);

    double sm = Math.abs(v1) + Math.abs(v2) + Math.abs(v3);
    if (sm < s_max) {
      v_max = u;
      s_max = sm;
    }
  }

  double[] u = v_max;
  v = u;

  v1 = Math.pow((u[0] - p1[0]), 2) + Math.pow((u[1] - p1[1]), 2) - Math.pow(p1[2], 2);
  v2 = Math.pow((u[0] - p2[0]), 2) + Math.pow((u[1] - p2[1]), 2) - Math.pow(p2[2], 2);
  v3 = Math.pow((u[0] - p3[0]), 2) + Math.pow((u[1] - p3[1]), 2) - Math.pow(p3[2], 2);
  
  stroke(200);
  
  strokeWeight(0.25);
  point((float)u[0],(float)u[1]);
}
