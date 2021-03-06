package com.newtglobal.eFmFmRouter.routing;

import java.util.*;

import com.newtglobal.eFmFmRouter.clustering.Geocode;
import com.newtglobal.eFmFmRouter.data.JsonDepot;
import com.newtglobal.eFmFmRouter.data.JsonVehicle;
import com.newtglobal.eFmFmRouter.data.Settings;

public final class Depot {
	private ArrayList<Vehicle> vehicleList;
	private int content;
	private Geocode depotLocation;
	private String name;
	private static int numberOfDepots = 0;
	private String depotId;

	public ArrayList<Vehicle> getVehicleList() {
		return vehicleList;
	}

	public Geocode getDepotLocation() {
		return depotLocation;
	}

	public String getName() {
		return name;
	}

	public static int getNumberOfDepots() {
		return numberOfDepots;
	}

	public String getDepotId() {
		return depotId;
	}

	public Depot(Geocode depot_location, String name)
	{
		content = 0;
		vehicleList = new ArrayList<Vehicle>();
		this.depotLocation = depot_location;
		this.name = name;
		numberOfDepots += 1;
		depotId = numberOfDepots + "";
	}
	
	public Depot(JsonDepot D) {
		numberOfDepots += 1;
		vehicleList = new ArrayList<Vehicle>();
		this.depotLocation = new Geocode(D.location);
		this.name = D.name;
		this.depotId = D.depot_id;
		this.content = 0;
		
		for (JsonDepot.JsonVehicleShed S : D.vehicle_type) {
			VehiclePrototype vehicleType = VehiclePrototype.getVehicleType(S.vehicle);
			if (vehicleType == null) {
				vehicleType = new VehiclePrototype(S.vehicle);
			}
			this.addVehicle(vehicleType, S.quantity);
		}
	}
	
	public Depot(JsonVehicle V, Settings settings) {
		numberOfDepots += 1;
		vehicleList = new ArrayList<Vehicle>();
		this.depotLocation = new Geocode(V.startLocation);
		this.name = V.vehicle_type.vehicle_type_name + V.vehicle_id;
		this.depotId = V.vehicle_id;
		this.content = 0;
		VehiclePrototype vehicleType = VehiclePrototype.getVehicleType(V.vehicle_type);
		if (vehicleType == null) {
			vehicleType = new VehiclePrototype(V.vehicle_type);
		}
		this.vehicleList.add(new Vehicle(vehicleType, V, settings));
	}
	
	public boolean isEqual(Depot D) {
		return (depotId == D.depotId);
	}
	
	public void addVehicle(VehiclePrototype V, int quantity)
	{
		for(int i = 1; i <= quantity; i++) {
			content += 1;
			Vehicle V2;
			V2 = new Vehicle(V, depotLocation, V.getName() + "_" + content + "_" + name);
			vehicleList.add(V2);
		}
	}
	
	//returns true if vehicle to remove is found
	public boolean removeVehicle(jsprit.core.problem.vehicle.Vehicle vehicle) {
		Vehicle VR = null;
		for(Vehicle V2 : vehicleList) {
			if(vehicle.equals(V2.getVehicleInstance())) {
				VR = V2;
				break;
			}
		}
		if (VR!= null) {
			vehicleList.remove(VR);
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean containsVehicle(jsprit.core.problem.vehicle.Vehicle vehicle) {
		for (Vehicle V : vehicleList) {
			if (V.getVehicleInstance().equals(vehicle)) {
				return true;
			}
		}
		return false;
	}
}
