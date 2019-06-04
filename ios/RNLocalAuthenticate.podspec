
Pod::Spec.new do |s|
  s.name         = "RNLocalAuthenticate"
  s.version      = "1.0.0"
  s.summary      = "RNLocalAuthenticate"
  s.description  = <<-DESC
                  RNLocalAuthenticate
                   DESC
  s.homepage     = ""
  s.license      = "MIT"
  # s.license      = { :type => "MIT", :file => "FILE_LICENSE" }
  s.author             = { "author" => "author@domain.cn" }
  s.platform     = :ios, "7.0"
  s.source       = { :git => "https://github.com/author/RNLocalAuthenticate.git", :tag => "master" }
  s.source_files  = "RNLocalAuthenticate/**/*.{h,m}"
  s.requires_arc = true


  s.dependency "React"
  #s.dependency "others"

end

  