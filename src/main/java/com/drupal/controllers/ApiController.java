package com.drupal.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;

import com.drupal.StudentRestApiApplication;
import com.drupal.dao.AnswerRepo;
import com.drupal.dao.InstituteRepo;
import com.drupal.dao.TokenRepo;
import com.drupal.dao.UserRepo;
import com.drupal.dao.VerificationTokenRepo;
import com.drupal.dao.VideoRepo;
import com.drupal.events.OnRegistrationSuccessEvent;
import com.drupal.models.Answer;
import com.drupal.models.Institute;
import com.drupal.models.Interests;
import com.drupal.models.Token;
import com.drupal.models.User;
import com.drupal.models.VerificationToken;
import com.drupal.models.Video;
import com.drupal.services.AES;
import com.drupal.services.EmailSenderService;

// TODO return all JSON error responses explicitly (not by res.sendError as it doesn't work on external server(not embedded))


@Controller
public class ApiController {

	@Autowired
	InstituteRepo instituteRepo;
	
	@Autowired
	VerificationTokenRepo verificationTokenRepo;

	@Autowired
	private ApplicationEventPublisher eventPublisher;

	@Autowired
	EmailSenderService emailSenderService;

	@Autowired
	TokenController tokenController;
	
	@Autowired
	AnswerRepo answerRepo;

	@Autowired
	TokenRepo tokenRepo;

	@Autowired
	UserController userController;

	@Autowired
	UserRepo userRepo;

	@Autowired
	VideoRepo videoRepo;

	@Autowired
	FileController fileController;

	@RequestMapping(path = "login", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public String login(@RequestPart String email, @RequestPart String password, HttpServletResponse res) {
		User user = userRepo.findByEmail(email);
		Institute inst = instituteRepo.findByEmail(email);
		if (user != null ||  inst!=null) {
			if(user!=null && inst==null) {
				if (user.isEmailVerified()) {
					Token t = tokenRepo.findByUserId(user.getId());
					if (t != null) {
						try {
							res.sendError(400, "User already signed in other device");
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						return "";
					} else if (AES.encrypt(password, StudentRestApiApplication.SECRET_KEY).equals(user.getPassword())) {
						String name = user.getName();
						String age = String.valueOf(user.getAge());
						String phone = user.getPhone();
						String interests = user.getInterests().toString();
						Token token = tokenController.createToken(user.getId());
						return "{\"Token Id\": \""+token.getId()+"\",\"Name\":\""+name+"\",\"Age\":\""+age+"\",\"Phone\":\""+phone+"\",\"Interests\":\""+interests+"\"}";
					} else {
						try {
							res.sendError(403, "Incorrect password");
						} catch (IOException e) {
							e.printStackTrace();
						}
						return "Incorrect password";
					}
				} else {
					try {
						res.sendError(403, "Email not verified");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}else if(user==null && inst!=null) {
				if (inst.isEmailVerified()) {
					Token t = tokenRepo.findByUserId(inst.getId());
					if (t != null) {
						try {
							res.sendError(400, "User already signed in other device");
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						return "";
					} else if (AES.encrypt(password, StudentRestApiApplication.SECRET_KEY).equals(inst.getPassword())) {
						String name = inst.getName();
						Token token = tokenController.createToken(inst.getId());
						return "{\"Token Id\" : \"" + token.getId() + "\",\"Name\":\"" + name + "\"}";
					} else {
						try {
							res.sendError(403, "Incorrect password");
						} catch (IOException e) {
							e.printStackTrace();
						}
						return "Incorrect password";
					}
				} else {
					try {
						res.sendError(403, "Email not verified");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
			

		} else {
			try {
				res.sendError(400, "Email doesn't exist.Please signup first...");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@RequestMapping(path = "signup", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public String signUp(@RequestPart String email, @RequestPart String password, @RequestPart String name, @RequestPart String age, @RequestPart String phone, @ModelAttribute Interests interests,
			HttpServletResponse res) {
		System.out.println("inside signup");
		if (userRepo.findByEmail(email) == null) {
			User user = userController.postUser(name, email, password, age, phone, interests);
			try {
			eventPublisher.publishEvent(new OnRegistrationSuccessEvent(user.getId(), StudentRestApiApplication.RegistrationTypes.user));
			}
			catch(MailException e) {
				try {
					res.sendError(500, "Can't send the verification mail");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				return "{\"Error\" : \"Unable to send mail\"}";
			}
			return "{\"Success\" : \"User created successfully\"}";
//			return login(email, password, res);
		} else {
			try {
				res.sendError(400, "User with this email already exists");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return "{\"Error\" : \"User with this email already exists\"}";
		}
	}

	@RequestMapping(path = "videos", method = RequestMethod.GET)
	@ResponseBody
	public List<Video> getVideos(@RequestPart String tokenId, HttpServletRequest req, HttpServletResponse res) {
		String userId = tokenController.getUserIdFrom(tokenId);
		System.out.println(userId);
		if (userId.equals(StudentRestApiApplication.NOT_FOUND)) {
			try {
				res.sendError(404, "User associated with tokenId not found");
			} catch (IOException e) {
				System.out.println("catched");
				e.printStackTrace();
			}
			return null;
		}
		List<Video> videos = videoRepo.findByUserId(userId);
		return videos;
	}

	@RequestMapping(path = "logOut", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public String loggingOut(@RequestPart String tokenId, @RequestPart String email, HttpServletResponse res) {
		System.out.println("In Logout");
		if (tokenController.deleteToken(tokenId, email)) {
			return "{\"Success\" : \"Successfully Logged Out\"}";
		} else
			return "{\"Error\" : \"Cannot Log Out The User\"}";
	}

	@RequestMapping(path = "videos/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Resource> getVideo(@PathVariable String id, @RequestPart String tokenId,
			HttpServletRequest req, HttpServletResponse res) {
		Video v = videoRepo.findById(id).orElse(null);
		if (v == null) {
			try {
				res.sendError(404, "Video not found");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		String fileName = v.getPath().substring(v.getPath().lastIndexOf('\\') + 1, v.getPath().length());
		return fileController.downloadFile(fileName, req);

	}




	@RequestMapping("sendVerificationMail")
	@ResponseBody
	public String sendVerificationMail(@RequestParam String email) {
		User user = userRepo.findByEmail(email);
		String id ;
		if(user!=null) {
		id= userRepo.findByEmail(email).getId();
		eventPublisher.publishEvent(new OnRegistrationSuccessEvent(id,StudentRestApiApplication.RegistrationTypes.user));
		}
		else {
			id = instituteRepo.findByEmail(email).getId();
			eventPublisher.publishEvent(new OnRegistrationSuccessEvent(id,StudentRestApiApplication.RegistrationTypes.institute));

		}
		return "send verification mail";
	}

	@RequestMapping(path="confirmtoken", method=RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public String confirmToken(@RequestParam String type, @RequestParam String token, HttpServletResponse res) throws IOException {
		System.out.println("inside confirm token "+token);
		VerificationToken vToken = verificationTokenRepo.findByToken(token);
		if (vToken == null) {
			try {
				res.sendError(400, "Invalid verification token");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			Date curDate = new Date();
			Date expiryDate = vToken.getExpiryDate();
			if (curDate.before(expiryDate)) {
				String userId = vToken.getUserId();
				
				if(type.equalsIgnoreCase(StudentRestApiApplication.RegistrationTypes.user.toString())) {
					User user = userRepo.findById(userId).orElse(null);
					if (user == null) {
						try {
							res.sendError(400, "User account was deleted");
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					user.setEmailVerified(true);
					userRepo.save(user);
				}
				else {
					Institute ins = instituteRepo.findById(userId).orElse(null);
					if (ins == null) {
						try {
							res.sendError(400, "User account was deleted");
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					ins.setEmailVerified(true);
					instituteRepo.save(ins);
				}
				verificationTokenRepo.delete(vToken);
				return "{\"Success\":\"Email successfully verified\"}";
			}
			else {
				res.sendError(400, "Token already expired");
			}

		}

		return token;

	}
	
	
	@RequestMapping(path = "/getVideo/{id}", method = RequestMethod.GET)
	@ResponseBody public File getPreview2(@PathVariable("id") String id, HttpServletResponse response) {
	    try {
	    	System.out.println("HHHHHHHHHHHHHHHHHHHHHHHHHH");
	    	Video v = videoRepo.findById(id).orElse(null);
	    	if(v != null) {
	    		
	    		String path = v.getPath();
		        System.out.println(path);
		        File file = new File(path);
		        System.out.println("SSSSSSSSSSSSSSSSSSSSSSSSSSSSS");
		        response.setContentType("video/mp4");
		        response.setHeader("Content-Disposition", "attachment; filename="+file.getName().replace(" ", "_"));
		        InputStream iStream = new FileInputStream(file);
		        IOUtils.copy(iStream, response.getOutputStream());
//		        response.flushBuffer();
		        return file;
	    	}
	    } catch (Exception e) {
	        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
	    }
		return null;
	}
	
	@RequestMapping(path = "/getQuestions", method = RequestMethod.GET)
	@ResponseBody
	public List<String> getAllQuestions(@RequestParam String tokenId){
		List<String> result = new ArrayList<String>();
		Token t = tokenRepo.findById(tokenId).orElse(null);
		String uid = t.getUserId();
		User u = userRepo.findById(uid).orElse(null);
		String[] interests = u.getInterests();
		List<Video> videos = videoRepo.findAll();
		int vidLen = videos.size();
		int len = interests.length;
		for(int i = 0 ; i < len ; i++) {
			String cur = interests[i];
			for(int j = 0 ; j < vidLen ; j++) {
				Video v = videos.get(j); 
				List<String> tags = v.getTags();
				if(tags.contains(cur)) {
					if(!result.contains(v.getId()) && !v.getUserId().equals(uid) ) {
						System.out.println(v.getId());
						result.add(v.getPath());
					}
				}
			}
		}
		System.out.println(result.toString());
		return result;
	}
	
	
	@RequestMapping(path="/getProfileDetails", produces="application/json")
	@ResponseBody
	public String getProfileDetails(@RequestParam String email, HttpServletResponse res) {
		User user = userRepo.findByEmail(email);
		if(user==null) {
			try {
				res.sendError(400, "User with this email doesn't exist");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return "{\"Error\":\"User with this email does't exist\"}";
		}
		else {
			Object interests;
			if(user.getInterests()!=null) {
			interests = new JSONArray(Arrays.asList(user.getInterests()));
			}
			else {
				interests = "null";
			}
			return "{\"Name\":\""+ user.getName()+"\", \"Age\":\""+user.getAge()+"\",\"Phone\":\""+user.getPhone()+"\",\"ProfilePic\":\""+user.getProfilePic()+"\",\"Interests\":"+interests.toString()+", \"Email\":\""+email+"\"}";
		}
	}
	
	@RequestMapping("getAnswersList")
	@ResponseBody
	public List<String> getAnswersList(@RequestParam String tokenId, HttpServletResponse res){
		String userId  = tokenController.getUserIdFrom(tokenId);
		if(userId==null) {
			try {
				res.sendError(400, "Token is invalid or expired");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		
		List<Answer> answers = answerRepo.findByUserId(userId);
		List<String> results = new ArrayList<String>();
		for(Answer ans : answers) {
			results.add(ans.getPath());
		}
		System.out.println(results);
		return results;
	}
	
	@RequestMapping("getAnswersToMyQuestions")
	@ResponseBody
	public List<ArrayList<String>> getAnswersToMyQuestions(@RequestParam String tokenId, HttpServletResponse res){
		System.out.println("inside atomq");
		System.out.println("id "+ tokenId);
		String userId  = tokenController.getUserIdFrom(tokenId);
		if(userId==null) {
			System.out.println("user id is null");
			try {
				res.sendError(400, "Token is invalid or expired");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		List<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
		System.out.println(userId);
		List<Video> videos = videoRepo.findByUserId(userId);
		System.out.println("videos " + videos);
		for(Video v: videos) {
			List<Answer> curAnswers = answerRepo.findByQuestionId(v.getId());
			System.out.println(curAnswers);
			ArrayList<String>temp = new ArrayList<String>();
			temp.add(v.getPath());
			for(Answer ans: curAnswers) {
				temp.add(ans.getPath());
			}
			result.add(temp);
		}
		System.out.println(result);
		return result;
	}
	
	@RequestMapping(method=RequestMethod.GET ,path="/getType")
	@ResponseBody
	public String getType(@RequestParam String email, HttpServletResponse res) {
		System.out.println("IN GETTYPE--------------");
		String ret;
		User user;
		Institute inst;
		user=userRepo.findByEmail(email);
		
		inst=instituteRepo.findByEmail(email);
		System.out.println(email);
		if(user!=null)
		{
			System.out.println(user.toString());
			ret="User";
		}
		else if(inst!=null)
		{
			System.out.println(inst.toString());
			ret="institute";
		}
		else
		{
			ret="null";
		}
		return ret;
	}
	
		
}
