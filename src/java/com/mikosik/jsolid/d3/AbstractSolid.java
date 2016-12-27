package com.mikosik.jsolid.d3;

import static com.mikosik.jsolid.JSolid.vx;
import static com.mikosik.jsolid.JSolid.vy;
import static com.mikosik.jsolid.JSolid.vz;
import static java.util.stream.Collectors.toList;

import java.util.List;

import eu.mihosoft.vrl.v3d.CSG;
import eu.mihosoft.vrl.v3d.Plane;
import eu.mihosoft.vrl.v3d.Transform;

public abstract class AbstractSolid implements Solid {

  public List<Vector3> vertexes() {
    return toCsg().getPolygons().stream()
        .flatMap(x -> x.vertices.stream())
        .map(x -> x.position)
        .collect(toList());
  }

  public Solid transform(Transform transform) {
    return new CsgSolid(toCsg().transformed(transform));
  }

  public Solid plus(Solid solid) {
    CSG thisCsg = toCsg();
    if (thisCsg.getPolygons().size() == 0) {
      return solid;
    }
    CSG thatCsg = solid.toCsg();
    if (thatCsg.getPolygons().size() == 0) {
      return this;
    }

    return new CsgSolid(toCsg().union(solid.toCsg()));
  }

  public Solid plus(Solid solid, Alignment<?> alignment) {
    return plus(alignment.align(this, solid));
  }

  public Solid minus(Solid solid) {
    CSG thisCsg = toCsg();
    if (thisCsg.getPolygons().size() == 0) {
      return this;
    }
    CSG thatCsg = solid.toCsg();
    if (thatCsg.getPolygons().size() == 0) {
      return this;
    }
    return new CsgSolid(thisCsg.difference(thatCsg));
  }

  public Solid minus(Solid solid, Alignment<?> alignment) {
    return minus(alignment.align(this, solid));
  }

  public Solid intersect(Solid solid) {
    CSG thisCsg = toCsg();
    if (thisCsg.getPolygons().size() == 0) {
      return this;
    }
    CSG thatCsg = solid.toCsg();
    if (thatCsg.getPolygons().size() == 0) {
      return solid;
    }
    return new CsgSolid(toCsg().intersect(solid.toCsg()));
  }

  public Solid move(Vector3 position) {
    return transform(Transform.translate(position));
  }

  public Solid move(Anchor<?> anchor, double value) {
    return move(anchor.axis.v(value).minus(anchor.vectorIn(this)));
  }

  public Solid rotate(Vector3 direction, double angle) {
    return transform(rotationTransform(direction, angle));
  }

  private static Transform rotationTransform(Vector3 axis, double angle) {
    if (axis.equals(vx(1))) {
      return Transform.rotateX(angle);
    } else if (axis.equals(vy(1))) {
      return Transform.rotateY(angle);
    } else if (axis.equals(vz(1))) {
      return Transform.rotateZ(angle);
    } else {
      throw new IllegalArgumentException("Axis must be one of vx(1), vy(1), vz(1).");
    }
  }

  public Solid mirror(Vector3 planeNormal) {
    return transform(Transform.mirror(plane(planeNormal)));
  }

  private Plane plane(Vector3 planeNormal) {
    if (planeNormal.equals(vx(1))) {
      return Plane.YZ_PLANE;
    } else if (planeNormal.equals(vy(1))) {
      return Plane.XZ_PLANE;
    } else if (planeNormal.equals(vz(1))) {
      return Plane.XY_PLANE;
    } else {
      throw new IllegalArgumentException("planeNormal must be one of vx(1), vy(1), vz(1).");
    }
  }

  public Solid scale(Vector3 factor) {
    return transform(Transform.scale(factor));
  }

  public Solid convexHull() {
    return new CsgSolid(toCsg().hull());
  }

  public String toStl() {
    return toCsg().toStlString();
  }
}
