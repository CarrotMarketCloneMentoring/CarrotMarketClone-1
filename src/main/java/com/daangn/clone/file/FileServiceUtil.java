package com.daangn.clone.file;

import com.daangn.clone.common.response.ApiException;
import com.daangn.clone.common.response.ApiResponseStatus;
import com.daangn.clone.encryption.AES128;
import com.daangn.clone.item.Item;
import com.daangn.clone.item.dto.RegisterItemDto;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class FileServiceUtil {

    /** 인자로 넘어온 암호화된 이미지를 복호화 한 후 -> 그 경로에 저장된 이미지를 읽어와 응답으로 리턴 */
    public InputStreamResource getImage(String encryptedPath, AES128 aes128){

        // 1. 일단 url로 넘어온(현재 이상태는 AES128로 막 암호화한 결과와 같음 주의) 경로를 AES128 복호화 하여 원본 경로로 변환해야 함
        String originalPath = aes128.decrypt(encryptedPath);

        //2. 이제 얻게된 이미지의 원본 경로에서 이미지를 Read 하여 응답으로 보냄
        /** read할 파일과 연결된 InputStream을 인자로 받은 InputStreamResource를 HTTP응답으로 넘기면,
         * ResourceHttpMessageConverter가 동작하면서 해당 파일을 버퍼를 사용하여 read하여 그대로 응답으로 넘긴다.*/
        InputStreamResource in;
        try{
            in  = new InputStreamResource(new FileInputStream(originalPath));
        } catch (FileNotFoundException e){
            throw new ApiException(ApiResponseStatus.FAIL_GET_ITEM_IMAGE, "복호화에 성공한 원본 경로에 저장된 이미지가 없습니다.");
        }
        return in;
    }

    /** 인자로 넘어온 상품 이미지 경로를 암호화 하여 , 암호화된 경로 리스트를 넘기는 서비스 */
    public List<String> getEncryptedPathList(Item item, String sampleDir, AES128 aes128){

        //i) 함께 저장된 상품 이미지가 없으면 -> 샘플 이미지의 경로를 암호화 하여 보냄
        if(CollectionUtils.isEmpty(item.getItemImageList())){
            String encryptedPath = aes128.encrypt(sampleDir);
            String finalEncodingPath = URLEncoder.encode(encryptedPath, StandardCharsets.UTF_8);
            return List.of(finalEncodingPath);
        }

        // ii) 그렇지 않고 함께 저장된 상품 이미지가 한장이라도 있으면 -> 그 상품 이미지 경로를 암호화 하여 보냄
        return item.getItemImageList().stream()
                .map(ii -> aes128.encrypt(ii.getPath()))
                .map(e -> URLEncoder.encode(e, StandardCharsets.UTF_8))
                .collect(Collectors.toList());
    }

    /** 인자로 넘어온 이미지들의 저장 경로를 -> 함께 인자로 넘어온 인자들을 가지고 생성하여 -> 그 경로 리스트를 반환하는 메소드 */
    public List<String> getPathList(String fileDir, RegisterItemDto registerItemDto, Long sellerMemberId, Long itemId){

        //이렇게 만든 pathList에는 각 이미지가 로컬에 저장될 절대경로가 담겨 있음
        return List.of(0,1,2,3,4).stream()
                .filter(i -> i<registerItemDto.getImageList().size())
                .map(i -> getImageContent(fileDir, sellerMemberId, itemId, (i+1),
                        FilenameUtils.getExtension(registerItemDto.getImageList().get(i).getOriginalFilename())))
                .collect(Collectors.toList());

    }

    /** 인자로 넘어온 이미지들을 로컬에 저장하는 기능*/
    public void saveImages(String fileDir, RegisterItemDto registerItemDto, List<String> pathList,
                           Long sellerMemberId, Long itemId){

        makeDirForItem(fileDir, sellerMemberId, itemId);

        //(3). 한장씩 해당 폴더에 이미지를 저장
        List.of(0,1,2,3,4).stream()
                .filter(i -> i<pathList.size())
                .forEach(i -> saveImageFile(registerItemDto.getImageList().get(i), pathList.get(i)));

    }

    /** 인자로 넘어온 모든 파일의 확장자가 jpeg 또는 png 이면 true를 , 그렇지 않으면 false를 리턴*/
    public boolean checkExtension(List<MultipartFile> fileList){

        return fileList.stream()
                .map(f -> FilenameUtils.getExtension(f.getOriginalFilename()))
                .allMatch(e -> e.equals("jpeg")||e.equals("png"));
    }

    /** idx 번째 사진이 저장된 로컬 path를 알려주는 서비스 (idx는 1번부터 시작)*/
    private String getImageContent(String fileDir, Long memberId, Long itemId, int idx, String ext){
        return (getDirForItem(fileDir, memberId, itemId) + File.separator + "image" + idx + "." + ext);
    }

    /** 이 상품 사진을 저장할 디렉터리의 경로를 알려주는 서비스*/
    private String getDirForItem(String fileDir, Long memberId , Long itemID){
        return fileDir + File.separator + getDirForToday() + File.separator + ("member" + memberId + "_" + "item" + itemID);
    }


    /** 오늘 날짜 디렉터리 이름을 알려주는 메서드 */
    private String getDirForToday(){
        return LocalDateTime.now().getYear() + "_" +(LocalDateTime.now().getMonth().getValue()) + "_"  + LocalDateTime.now().getDayOfMonth();
    }


    /** 실제로 해당 경로에 이미지를 저장하는 메서드 */
    private void saveImageFile(MultipartFile file, String path){
        try {
            file.transferTo(Paths.get(path));
        } catch (IOException e){
            throw new ApiException(ApiResponseStatus.FAIL_SAVE_IMAGE, "상품 이미지 저장에 실패했습니다 : " + e.getMessage());
        } catch (IllegalStateException e){
            throw new ApiException(ApiResponseStatus.FAIL_SAVE_IMAGE, "IllegalStateException에 의해 상품 이미지 저장에 실패했습니다");
        }
    }

    /** 오늘의 날짜별 디렉터리를 생성하는 메서드*/
    public void makeDirForToday(String subDir){
        LocalDateTime now = LocalDateTime.now();
        String today = now.getYear() + "_" +(now.getMonth().getValue()) + "_"  + now.getDayOfMonth();
        Path todayPath = Paths.get(subDir + File.separator+ today);
        try{
            Files.createDirectories(todayPath);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    /** 이미지를 저장할 디렉터리를 생성하는 메서드 */
    public void makeDirForItem(String fileDir, Long memberId, Long itemId){

        try{
            // 오늘 날짜에 대한 데릭터리가 혹시나 없다면 -> 오늘 날짜에 대한 디렉터리를 먼저 생성
            //Optional.of(Files.exists(Paths.get(fileDir + File.separator +getDirForToday()))).
            if(Files.exists(Paths.get(fileDir + File.separator + getDirForToday())) == false){
                Files.createDirectory(Paths.get(fileDir + File.separator + getDirForToday()));
            }

            // 이후 상품이미지를 저장할 디렉터리가 존재하지 않는다면 -> 오늘 날짜 디렉터리 안에 새로 생성하면 되고
            if(Files.exists(Paths.get(getDirForItem(fileDir,memberId,itemId))) == false){
                Files.createDirectory(Paths.get(getDirForItem(fileDir, memberId, itemId)));
            }
            // 혹시나 상품 이미지를 저장할 디렉터리가 이미 존재한다면 -> 이는 이전 요청의 IOException에 의해 재요청 하는 경우이므로 , 해당 디렉터리안의 사진을 모두 지운다.
            else{
                FileUtils.cleanDirectory(new File(getDirForItem(fileDir,memberId, itemId)));
            }

        } catch (IOException e){
            throw new ApiException(ApiResponseStatus.FAIL_SAVE_IMAGE, "상품이미지 저장을 위한 디렉터리 생성에 실패했습니다 : " + e.getMessage());
        }
    }






}
