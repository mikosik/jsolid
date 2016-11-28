package com.mikosik.jsolid;

import static com.mikosik.jsolid.d2.Vector2.vector2;
import static eu.mihosoft.vrl.v3d.ext.quickhull3d.HullUtil.hull;
import static java.util.Arrays.asList;

import com.mikosik.jsolid.d1.Range;
import com.mikosik.jsolid.d2.Circle;
import com.mikosik.jsolid.d2.ConvexPolygon;
import com.mikosik.jsolid.d2.Polygon;
import com.mikosik.jsolid.d2.Rectangle;
import com.mikosik.jsolid.d2.RegularPolygon;
import com.mikosik.jsolid.d2.Vector2;
import com.mikosik.jsolid.d3.Box;
import com.mikosik.jsolid.d3.CsgSolid;
import com.mikosik.jsolid.d3.Cylinder;
import com.mikosik.jsolid.d3.Pipe;
import com.mikosik.jsolid.d3.Prism;
import com.mikosik.jsolid.d3.Rod;
import com.mikosik.jsolid.d3.Solid;

import eu.mihosoft.vrl.v3d.CSG;
import eu.mihosoft.vrl.v3d.Vector3d;

public class JSolid {
  public static Range range() {
    return new Range();
  }

  public static Range range(double v1, double v2) {
    return new Range(v1, v2);
  }

  public static Range range(double vd) {
    return new Range().vd(vd);
  }

  public static Vector2 v(double x, double y) {
    return vector2(x, y);
  }

  public static Vector3d v(double x, double y, double z) {
    return new Vector3d(x, y, z);
  }

  public static Vector3d v(Vector2 v, double z) {
    return new Vector3d(v.x, v.y, z);
  }

  public static Vector3d v0() {
    return v(0, 0, 0);
  }

  public static Vector3d vx() {
    return vx(1);
  }

  public static Vector3d vx(double x) {
    return v(x, 0, 0);
  }

  public static Vector3d vy() {
    return vy(1);
  }

  public static Vector3d vy(double y) {
    return v(0, y, 0);
  }

  public static Vector3d vz() {
    return vz(1);
  }

  public static Vector3d vz(double z) {
    return v(0, 0, z);
  }

  public static Polygon regularPolygon(double radius, int vertexCount) {
    return new RegularPolygon(radius, vertexCount);
  }

  public static Circle circle(double radius) {
    return new Circle(radius);
  }

  public static Rectangle rectangle() {
    return new Rectangle();
  }

  public static Rectangle rectangle(double xRange, double yRange) {
    return new Rectangle(range(xRange), range(yRange), 0);
  }

  public static Polygon convexPolygon(Vector2... vertexes) {
    return new ConvexPolygon(vertexes);
  }

  public static Prism prism() {
    return new Prism();
  }

  public static Prism prism(Polygon base, Range zd) {
    return new Prism(base, zd);
  }

  public static Solid nothing() {
    return new CsgSolid(CSG.fromPolygons());
  }

  public static Box box() {
    return new Box();
  }

  public static Box box(double xRange, double yRange, double zRange) {
    return box().x(range(xRange)).y(range(yRange)).z(range(zRange));
  }

  public static Cylinder cylinder() {
    return new Cylinder();
  }

  public static Cylinder cylinder(double radius, double zd) {
    return new Cylinder(circle(radius), range().vd(zd));
  }

  public static Solid convexHull(Vector3d... vertexes) {
    return new CsgSolid(hull(asList(vertexes)));
  }

  public static Rod rod() {
    return new Rod();
  }

  public static Pipe pipe() {
    return new Pipe();
  }

  public static Solid quadrupleInZPlane(Solid leg) {
    Solid vxClone = leg.plus(leg.mirror(vx()));
    return vxClone.plus(vxClone.mirror(vy()));
  }
}
