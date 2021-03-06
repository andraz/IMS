/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ims.servlet.image;

import ims.entity.Image;
import ims.management.ImageManagement;
import ims.management.LoginManagement;
import ims.management.ResizeImage;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.ServerException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;

import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 *This servlet allow contributor to upload a new image 
 * @author andrazhribernik
 */
@WebServlet(name = "UploadImage", urlPatterns = {"/UploadImage"})
public class UploadImage extends HttpServlet {


    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.print("Upload image GET");
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        LoginManagement lm = new LoginManagement(request.getSession());
        lm.userPermissionForThisPage(response, new String[]{"contributor"});
        
        // Verify the content type
        String contentType = request.getContentType();
        ServletContext cntx= getServletContext();
        File file ;
        int maxFileSize = 5000 * 1024;
        int maxMemSize = 5000 * 1024;
                        
        if ((contentType.indexOf("multipart/form-data") >= 0)) {

           DiskFileItemFactory factory = new DiskFileItemFactory();
           // maximum size that will be stored in memory
           factory.setSizeThreshold(maxMemSize);
           // Location to save data that is larger than maxMemSize.
           factory.setRepository(new File(cntx.getRealPath("temp")));

           // Create a new file upload handler
           ServletFileUpload upload = new ServletFileUpload(factory);
           // maximum file size to be uploaded.
           upload.setSizeMax( maxFileSize );
           try{ 
               
              ImageManagement im = new ImageManagement();
              // Parse the request to get file items.
              List fileItems = upload.parseRequest(request);

              // Process the uploaded file items
              Iterator i = fileItems.iterator();
              Integer price = new Integer(0);
              String name = "";
              while ( i.hasNext () ) 
              {
                    FileItem item = (FileItem)i.next();
                    if(!item.isFormField()){
                        name = new File(item.getName()).getName();
                        //check if image with the same name exists
                        if(!im.isImageNameEmpty(name, lm.getUser())){
                            response.sendRedirect("./uploadImage.jsp?fileMessage=This image name already exists.");
                            return;
                        }
                        
                        String extension = "";
                        if(name.matches("(.*)\\.jpg$")){
                            extension = "jpg";
                        }
                        else if(name.matches("(.*)\\.png$")){
                            extension = "png";
                        }
                        else{
                            //if extension is not .png or .jpg inform user about an error.
                            response.sendRedirect("./uploadImage.jsp?fileMessage=Wrong file format.");
                            return;
                        }
                        
                        for(String size : new String[]{"300","600","1200"}){
                            String imagePath = "Images"+File.separator+size+File.separator+lm.getUser().getUsername()+File.separator+name;
                            String filename = cntx.getRealPath(imagePath);

                            System.out.println(filename);
                            File newImage = new File(filename);
                            if (!newImage.getParentFile().exists())
                                newImage.getParentFile().mkdirs();
                            
                            if(!newImage.canWrite()){
                                newImage.setWritable(true);
                            }
                            BufferedImage originalImage = ImageIO.read(item.getInputStream());
                            int type = originalImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
                            BufferedImage resizeImagePng = ResizeImage.resizeImage(originalImage, type, Integer.valueOf(size));
                            //save image to file system
                            ImageIO.write(resizeImagePng, extension, newImage);
                        }
                    }
                    else{
                        if(item.getFieldName().equals("price")){

                            try{
                                Double inputPrice = Double.valueOf(item.getString());
                                price = ((Double)(inputPrice.doubleValue() * 100)).intValue();
                            }
                            catch (Exception e){
                                response.sendRedirect("./uploadImage.jsp?priceMessage=Incorect price format.");
                                return;
                            }
                        }
                    }
              }
              //store new image into database with specified price
                Image img = new Image();
                img.setDate(new Date());
                img.setName(name);
                img.setUseridUser(lm.getUser());
                img.setPrice(price);
                im.addImage(img);
              
           }catch(Exception ex) {
                ex.printStackTrace();
                response.sendRedirect("./uploadImage.jsp?fileMessage=Error during uploading file.");
                return;
           }
        }else{
            throw new ServerException("This Servlet only handles file upload request");
        }
        response.sendRedirect("./uploadImage.jsp?successMessage=You uploaded new image successfully.");
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
