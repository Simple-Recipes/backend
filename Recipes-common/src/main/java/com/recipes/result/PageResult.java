package com.recipes.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * Encapsulate paging query results
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageResult<T> implements Serializable {

    private long total; // Total number of records

    private List<T> records; // Current page data collection

}
