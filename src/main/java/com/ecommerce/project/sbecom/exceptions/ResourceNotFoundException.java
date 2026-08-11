package com.ecommerce.project.sbecom.exceptions;

public class ResourceNotFoundException extends RuntimeException{

   private String resourceName;
   private String field;
   private String filedName;
   private Long filedId;

   public void ResourceNotFoundException() {
   }

   public ResourceNotFoundException(String resourceName, String field, String filedName) {

      super(String.format("%s not found with %s: %s",resourceName,field,filedName));
      this.resourceName = resourceName;
      this.field=field;
      this.filedName = filedName;

   }

   public ResourceNotFoundException(String resourceName, String field, Long filedId) {
      super(String.format("%s not found with %s: %s",resourceName,field,filedId));
      this.resourceName = resourceName;
      this.field = field;
      this.filedId = filedId;
   }
}
