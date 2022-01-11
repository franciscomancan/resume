package com.cs.stadosweb.domain;

public interface Entity extends java.io.Serializable
{
  /**
   * Sets the value of property id.
   *  
   * @returns the value of property id.
   */
  public void setId( java.lang.Long id ); 
  
  /**
   * Sets the value of property createdDate.
   *  
   * @returns the value of property createdDate.
   */
  
  public void setCreatedDate( java.util.Date createdDate ); 
  
  /**
   * Sets the value of property createdBy.
   *  
   * @returns the value of property createdBy.
   */
  
  public void setCreatedBy( java.lang.String createdBy ); 
  
  /**
   * Sets the value of property modifiedDate.
   *  
   * @returns the value of property modifiedDate.
   */
  
  public void setModifiedDate( java.util.Date modifiedDate ); 
  
  /**
   * Sets the value of property modifiedBy.
   *  
   * @returns the value of property modifiedBy.
   */
  
  public void setModifiedBy( java.lang.String modifiedBy ); 
  
  /**
   * Returns the value of property id.
   *
   * @returns the value of property id.
   */
  
  public java.lang.Long getId();
  
  /**
   * Returns the value of property createdDate.
   *
   * @returns the value of property createdDate.
   */
  
  public java.util.Date getCreatedDate();
  
  /**
   * Returns the value of property createdBy.
   *
   * @returns the value of property createdBy.
   */
  
  public java.lang.String getCreatedBy();
  
  /**
   * Returns the value of property modifiedDate.
   *
   * @returns the value of property modifiedDate.
   */
  
  public java.util.Date getModifiedDate();
  
  /**
   * Returns the value of property modifiedBy.
   *
   * @returns the value of property modifiedBy.
   */
  
  public java.lang.String getModifiedBy();
  
}