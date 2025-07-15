package com.example.uavapplication.model;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PointLatLngAlt {

    @JsonProperty("latitude")
    public double latitude; //纬度

    @JsonProperty("longitude")
    public double longitude;//经度

    @JsonProperty("alt")
    public double alt;//相对地面的高度

    @JsonProperty("keeptime")
    public int keeptime;//停留时间
}
