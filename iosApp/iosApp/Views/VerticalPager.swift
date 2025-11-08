import SwiftUI

struct VerticalPager<Content: View>: UIViewRepresentable {
    var totalPages: Int
    var initialPage: Int
    let onPageChanged: (Int) -> Void
    @ViewBuilder var content: () -> Content
    
    func makeUIView(context: Context) -> UIScrollView {
        let scrollView = PageScrollView(initialPage: initialPage, onPageChanged: onPageChanged)
        let controller = UIHostingController(rootView: content())
        
        controller.view.translatesAutoresizingMaskIntoConstraints = false
        context.coordinator.hostingController = controller
        
        scrollView.addSubview(controller.view)
        
        NSLayoutConstraint.activate([
            controller.view.leadingAnchor.constraint(equalTo: scrollView.leadingAnchor),
            controller.view.trailingAnchor.constraint(equalTo: scrollView.trailingAnchor),
            controller.view.topAnchor.constraint(equalTo: scrollView.topAnchor),
            controller.view.bottomAnchor.constraint(equalTo: scrollView.bottomAnchor),
            controller.view.widthAnchor.constraint(equalTo: scrollView.widthAnchor),
            controller.view.heightAnchor.constraint(equalTo: scrollView.heightAnchor, multiplier: CGFloat(totalPages))
        ])
        
        return scrollView
    }
    
    func updateUIView(_ uiView: UIScrollView, context: Context) {
        context.coordinator.hostingController.rootView = content()
    }
    
    func makeCoordinator() -> Coordinator {
        Coordinator(self)
    }
    
    class Coordinator: NSObject {
        let parent: VerticalPager
        var hostingController: UIHostingController<Content>!
        
        init(_ parent: VerticalPager) {
            self.parent = parent
        }
    }
}

class PageScrollView: UIScrollView, UIScrollViewDelegate {
    private var frameHeight: CGFloat = 0
    private var currentPage: Int
    
    let initialPage: Int
    let onPageChanged: (Int) -> Void
    
    init(initialPage: Int, onPageChanged: @escaping (Int) -> Void) {
        self.currentPage = initialPage
        self.initialPage = initialPage
        self.onPageChanged = onPageChanged
        super.init(frame: .zero)
        
        self.showsVerticalScrollIndicator = false
        self.showsHorizontalScrollIndicator = false
        self.isScrollEnabled = true
        self.isPagingEnabled = true
        self.bounces = true
        
        self.delegate = self
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    override func layoutSubviews() {
        super.layoutSubviews()
        adjustContentOffset()
    }
    
    func adjustContentOffset() {
        if frameHeight != frame.height {
            frameHeight = frame.height
            let offset = CGFloat(currentPage) * frameHeight
            self.setContentOffset(CGPoint(x: 0, y: offset), animated: false)
        }
    }
    
    func scrollViewDidScrollToTop(_ scrollView: UIScrollView) {
        currentPage = 0
        onPageChanged(0)
    }
    
    func scrollViewDidEndDecelerating(_ scrollView: UIScrollView) {
        if frame.height == frameHeight {
            let newPage = Int(round(scrollView.contentOffset.y / scrollView.frame.height))
            
            if newPage != currentPage {
                currentPage = newPage
                onPageChanged(newPage)
            }
        }
    }
}
