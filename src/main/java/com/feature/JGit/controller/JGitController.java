package com.feature.JGit.controller;

import com.feature.JGit.entity.CommitDetails;
import com.feature.JGit.entity.FileDetails;
import com.feature.JGit.entity.ModifiedList;
import com.feature.JGit.entity.FileWithContent;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.transport.URIish;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.util.io.DisabledOutputStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
public class JGitController {
    @Value("${file.path.name}")
    private String filePath;
    @GetMapping("/getMapList")
    public String getMapList() {
        return "running";
    }
    @GetMapping("/gitLog")
    @ResponseBody
    public List<CommitDetails> getCommitDetails(@RequestParam String className) throws IOException {
        List<CommitDetails> commitDetailsList = new ArrayList<>();
        String repositoryPath = "H:\\repository\\ixmcm";
        String filePathVariable = filePath+className;
        Repository repository = Git.open(new File(repositoryPath)).getRepository();
        try {
            try (Git git = new Git(repository)) {
                Iterable<RevCommit> logs = git.log().addPath(filePathVariable).call();
                System.out.println("logs"+logs);
                for (RevCommit commit : logs) {
                    CommitDetails commitDetails = new CommitDetails();
                    commitDetails.setCommitId(commit.getId().getName());
                    commitDetails.setAuthor(commit.getAuthorIdent().getName());
                    commitDetails.setEmail(commit.getAuthorIdent().getEmailAddress());
                    commitDetails.setDate(commit.getAuthorIdent().getWhen().toString());
                    commitDetails.setMessage(commit.getFullMessage());
                    System.out.println("details"+commitDetails);
                    commitDetailsList.add(commitDetails);

                }
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return commitDetailsList;
    }
    @GetMapping("/modifiedFileList")
    @ResponseBody
    public List<ModifiedList> getModifiedFileList(@RequestParam String commitId) throws GitAPIException {
        List<ModifiedList> response = new ArrayList<>();
        ModifiedList modifiedFile =new ModifiedList();
        FileWithContent fileWithContent=new FileWithContent();
        String repositoryPath = "H:\\repository\\ixmcm";
        String filePathVariable="";
        try (Repository repository = Git.open(new File(repositoryPath)).getRepository()) {
            Git git = new Git(repository);
            ObjectId head = ObjectId.fromString(commitId);
            RevWalk rw = new RevWalk(repository);
            RevCommit revCommitId = rw.parseCommit(head);
            DiffFormatter df = new DiffFormatter(DisabledOutputStream.INSTANCE);
            df.setRepository(repository);
            df.setDiffComparator(RawTextComparator.DEFAULT);
            df.setDetectRenames(true);
            int count = revCommitId.getParentCount();
            if(count > 0) {
                RevCommit parent = rw.parseCommit(revCommitId.getParent(0).getId());
                List<DiffEntry> diffs = df.scan(parent.getTree(), revCommitId.getTree());
                for (DiffEntry diff : diffs) {
//                    System.out.println("diffs"+diff);
                    filePathVariable = diff.getNewPath();
                    fileWithContent.setFilePath(filePathVariable);
                    ObjectLoader loader = repository.open(head);
                    String fileContent = new String(loader.getBytes());
                    fileWithContent.setFileContent(fileContent);
                    modifiedFile.setFiles(fileWithContent);
                    modifiedFile.setAuthor(String.valueOf(revCommitId.getAuthorIdent()));
                    modifiedFile.setCommitMessage(revCommitId.getFullMessage());
                    response.add(modifiedFile);
                }
            }
            else
            {

            }
        } catch (IOException  e) {
            e.printStackTrace();
        }
        return response;
    }
    /*@GetMapping("/commitFile")
    @ResponseBody
    public void getModifiedFileList() throws GitAPIException{
        String repositoryPath = "H:\\repository\\ixmcm";
//        String filePath="Documents\\api-doc";
//        File file = new File("Documents\\api-doc");
        try (Repository repository = Git.open(new File(repositoryPath)).getRepository()) {
            Git git = new Git(repository);
//            git.add().addFilepattern(file.getName()).call();
//            RevCommit test_commit = git.commit().setMessage("file commited").call();
            git.add().addFilepattern("ixMCM/src/main/java/com/fhlbsf/ix/transformer/FHLB_CommCommerce.java").call();
//            git.commit().setMessage("file commited").call();
            System.out.println("commiting");
            git.commit()
//                    .setAll(true)
                    .setMessage("testing the commit")
                    .call();

            System.out.println("remote adding");
            git.remoteAdd()
                    .setName("origin/feature/jGit-feature")
                    .setUri(new URIish("https://stash.fhlbss.com/scm/col/ixmcm.git"))
                    .call();

            System.out.println("pushing");
            git.push()
                    .setRemote("https://stash.fhlbss.com/scm/col/ixmcm.git")
                    .setCredentialsProvider(
                            new UsernamePasswordCredentialsProvider("shruthie", "MondayFriday@123")
                    )
                    .call();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }*/
    @GetMapping("/listsFile")
    public List<FileDetails> getFileList() {
        List<FileDetails> fileDetailsList = new ArrayList<>();
        String repositoryPath = "H:\\dummy\\book-store-project";
        try(Repository repository = Git.open(new File(repositoryPath)).getRepository())  {

            Git git = new Git(repository);
            String folderPath="src/main/java/com/bittercode/model";
            ObjectId headId = repository.resolve("HEAD");
            RevWalk walk = new RevWalk(repository);
            RevCommit commit = walk.parseCommit(headId);
            RevTree tree = commit.getTree();
            System.out.println("Having tree: " + tree);
            // now use a TreeWalk to iterate over all files in the Tree recursively
            // you can set Filters to narrow down the results if needed
            TreeWalk treeWalk = new TreeWalk(repository);
            treeWalk.addTree(tree);
            treeWalk.setRecursive(true);
            while (treeWalk.next()) {
                String path = treeWalk.getPathString();
                if (path.startsWith(folderPath)) {
                    FileDetails fileDetails = new FileDetails();
                    String fileName = treeWalk.getNameString();
                    fileDetails.setId(UUID.randomUUID().toString());
                    fileDetails.setUserName(commit.getAuthorIdent().getName());
                    fileDetails.setMapName(fileName.substring(0,fileName.lastIndexOf('.')));
                    fileDetails.setCreateDate(new Date(commit.getCommitTime() * 1000L));
                    fileDetailsList.add(fileDetails);

                }
            }


        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return fileDetailsList;
    }

}
